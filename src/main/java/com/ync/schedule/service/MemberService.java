package com.ync.schedule.service;

import com.ync.schedule.domain.Event;
import com.ync.schedule.domain.Member;
import com.ync.schedule.dto.LeaveBalanceDto;
import com.ync.schedule.dto.MemberDto;
import com.ync.schedule.mapper.EventMapper;
import com.ync.schedule.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberMapper memberMapper;
    private final EventMapper eventMapper;

    public MemberService(MemberMapper memberMapper, EventMapper eventMapper) {
        this.memberMapper = memberMapper;
        this.eventMapper = eventMapper;
    }

    public List<MemberDto> getAllMembers() {
        return memberMapper.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MemberDto> getActiveMembers() {
        return memberMapper.findByIsActiveTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MemberDto getMemberById(Long id) {
        Member member = memberMapper.findById(id);
        if (member == null) {
            throw new RuntimeException("Member not found: " + id);
        }
        return convertToDto(member);
    }

    @Transactional
    public MemberDto createMember(MemberDto dto) {
        // 입사일 기반 연차 자동 계산
        BigDecimal calculatedLeave = dto.getHireDate() != null
            ? calculateAnnualLeave(dto.getHireDate())
            : BigDecimal.valueOf(15.0);

        Member member = Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .departmentId(dto.getDepartmentId())
                .position(dto.getPosition())
                .hireDate(dto.getHireDate())
                .annualLeaveGranted(dto.getAnnualLeaveGranted() != null ? dto.getAnnualLeaveGranted() : calculatedLeave)
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .smtpPassword(dto.getSmtpPassword())
                .build();
        memberMapper.insert(member);
        return convertToDto(member);
    }

    @Transactional
    public MemberDto updateMember(Long id, MemberDto dto) {
        Member member = memberMapper.findById(id);
        if (member == null) {
            throw new RuntimeException("Member not found: " + id);
        }

        member.setName(dto.getName());
        member.setEmail(dto.getEmail());
        member.setPhone(dto.getPhone());
        member.setDepartmentId(dto.getDepartmentId());
        member.setPosition(dto.getPosition());
        member.setHireDate(dto.getHireDate());
        member.setIsActive(dto.getIsActive());
        member.setSmtpPassword(dto.getSmtpPassword());

        // 입사일이 있으면 연차 재계산
        if (dto.getHireDate() != null) {
            BigDecimal calculatedLeave = calculateAnnualLeave(dto.getHireDate());
            member.setAnnualLeaveGranted(calculatedLeave);
        } else if (dto.getAnnualLeaveGranted() != null) {
            member.setAnnualLeaveGranted(dto.getAnnualLeaveGranted());
        }

        memberMapper.update(member);
        return convertToDto(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        memberMapper.deleteById(id);
    }

    public LeaveBalanceDto getLeaveBalance(Long memberId, int year) {
        Member member = memberMapper.findById(memberId);
        if (member == null) {
            throw new RuntimeException("Member not found: " + memberId);
        }

        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        List<Event> approvedLeaves = eventMapper.findByMemberIdAndDateRange(memberId, startDate, endDate).stream()
                .filter(e -> "APPROVED".equals(e.getStatus().name()) && "LEAVE".equals(e.getEventType().name()))
                .collect(Collectors.toList());

        BigDecimal usedLeave = approvedLeaves.stream()
                .map(Event::getLeaveAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal remainingLeave = member.getAnnualLeaveGranted().subtract(usedLeave);

        return LeaveBalanceDto.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .department(member.getDepartment())
                .position(member.getPosition())
                .annualLeaveGranted(member.getAnnualLeaveGranted())
                .usedLeave(usedLeave)
                .remainingLeave(remainingLeave)
                .build();
    }

    public List<LeaveBalanceDto> getAllLeaveBalances(int year) {
        return memberMapper.findByIsActiveTrue().stream()
                .map(member -> getLeaveBalance(member.getId(), year))
                .collect(Collectors.toList());
    }

    private MemberDto convertToDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .departmentId(member.getDepartmentId())
                .department(member.getDepartment())
                .position(member.getPosition())
                .hireDate(member.getHireDate())
                .annualLeaveGranted(member.getAnnualLeaveGranted())
                .isActive(member.getIsActive())
                .smtpPassword(member.getSmtpPassword())
                .build();
    }

    /**
     * 입사일 기준으로 연차 일수를 자동 계산
     * - 기본 연차: 15일
     * - 1년 미만: 현재 월 기준 남은 월수만큼 (월 단위 비례)
     * - 3년차 이상: 1일 가산
     * - 그 이후: 매 근속 2년마다 1일 추가 가산
     * - 최대: 25일 (가산 휴가 최대 10일)
     * - 시행일: 2024.03.01
     */
    private BigDecimal calculateAnnualLeave(LocalDate hireDate) {
        if (hireDate == null) {
            return BigDecimal.valueOf(15.0);
        }

        LocalDate effectiveDate = LocalDate.of(2024, 3, 1);
        LocalDate today = LocalDate.now();

        // 시행일 이전 입사자는 시행일을 기준으로 계산
        LocalDate calculationDate = hireDate.isBefore(effectiveDate) ? effectiveDate : hireDate;

        // 근속 년수 계산
        long totalMonths = java.time.temporal.ChronoUnit.MONTHS.between(calculationDate, today);
        double yearsOfService = totalMonths / 12.0;

        BigDecimal annualLeave;

        // 1년 미만인 경우: 남은 월수 기준 비례 계산
        if (yearsOfService < 1.0) {
            // 현재 연도의 남은 월수 계산
            int currentMonth = today.getMonthValue();
            int remainingMonths = 12 - currentMonth + 1;
            annualLeave = BigDecimal.valueOf(remainingMonths);
        } else {
            // 기본 연차 15일
            annualLeave = BigDecimal.valueOf(15.0);

            // 3년차 이상: 1일 가산
            if (yearsOfService >= 3.0) {
                annualLeave = annualLeave.add(BigDecimal.valueOf(1.0));

                // 3년 이후 매 2년마다 1일 추가 가산
                double yearsAfterThree = yearsOfService - 3.0;
                int additionalDays = (int) (yearsAfterThree / 2.0);
                annualLeave = annualLeave.add(BigDecimal.valueOf(additionalDays));
            }

            // 최대 25일 제한
            if (annualLeave.compareTo(BigDecimal.valueOf(25.0)) > 0) {
                annualLeave = BigDecimal.valueOf(25.0);
            }
        }

        return annualLeave;
    }
}
