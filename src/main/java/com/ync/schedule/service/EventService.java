package com.ync.schedule.service;

import com.ync.schedule.domain.Approval;
import com.ync.schedule.domain.Event;
import com.ync.schedule.domain.Member;
import com.ync.schedule.dto.EventDto;
import com.ync.schedule.mapper.ApprovalMapper;
import com.ync.schedule.mapper.EventMapper;
import com.ync.schedule.mapper.MemberMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class EventService {

    private final EventMapper eventMapper;
    private final MemberMapper memberMapper;
    private final ApprovalMapper approvalMapper;
    private final EmailService emailService;

    public EventService(EventMapper eventMapper, MemberMapper memberMapper, ApprovalMapper approvalMapper, EmailService emailService) {
        this.eventMapper = eventMapper;
        this.memberMapper = memberMapper;
        this.approvalMapper = approvalMapper;
        this.emailService = emailService;
    }

    public List<EventDto> getAllEvents() {
        return eventMapper.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EventDto> getEventsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return eventMapper.findByDateRange(startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EventDto> getMemberEvents(Long memberId) {
        return eventMapper.findByMemberId(memberId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public EventDto getEventById(Long id) {
        Event event = eventMapper.findById(id);
        if (event == null) {
            throw new RuntimeException("Event not found: " + id);
        }
        return convertToDto(event);
    }

    @Transactional
    public EventDto createEvent(EventDto dto) {
        Member member = memberMapper.findById(dto.getMemberId());
        if (member == null) {
            throw new RuntimeException("Member not found: " + dto.getMemberId());
        }

        // SCHEDULE 타입은 바로 APPROVED, LEAVE 타입은 DRAFT
        Event.EventStatus initialStatus = dto.getEventType() == Event.EventType.SCHEDULE
                ? Event.EventStatus.APPROVED
                : Event.EventStatus.DRAFT;

        Event event = Event.builder()
                .memberId(dto.getMemberId())
                .eventType(dto.getEventType())
                .eventSubtype(dto.getEventSubtype())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .leaveAmount(dto.getLeaveAmount() != null ? dto.getLeaveAmount() : BigDecimal.ZERO)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(initialStatus)
                .department(dto.getDepartment())
                .position(dto.getPosition())
                .division(dto.getDivision())
                .build();

        eventMapper.insert(event);
        return convertToDto(event);
    }

    @Transactional
    public EventDto updateEvent(Long id, EventDto dto) {
        Event event = eventMapper.findById(id);
        if (event == null) {
            throw new RuntimeException("Event not found: " + id);
        }

        if (event.getStatus() != Event.EventStatus.DRAFT && event.getStatus() != Event.EventStatus.REJECTED) {
            throw new RuntimeException("Can only update draft or rejected events");
        }

        event.setEventType(dto.getEventType());
        event.setEventSubtype(dto.getEventSubtype());
        event.setStartDate(dto.getStartDate());
        event.setEndDate(dto.getEndDate());
        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());
        event.setLeaveAmount(dto.getLeaveAmount());
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setDepartment(dto.getDepartment());
        event.setPosition(dto.getPosition());
        event.setDivision(dto.getDivision());

        eventMapper.update(event);
        return convertToDto(event);
    }

    @Transactional
    public void deleteEvent(Long id) {
        try {
            System.out.println("=== deleteEvent called with id: " + id);

            Event event = eventMapper.findById(id);
            System.out.println("Event found: " + event);

            if (event == null) {
                throw new RuntimeException("Event not found: " + id);
            }

            System.out.println("Event type: " + event.getEventType() + ", Status: " + event.getStatus());

            // LEAVE 타입의 APPROVED는 삭제 불가, SCHEDULE은 APPROVED여도 삭제 가능
            if (event.getStatus() == Event.EventStatus.APPROVED && event.getEventType() == Event.EventType.LEAVE) {
                throw new RuntimeException("Cannot delete approved leave events");
            }

            // Delete related approvals first
            System.out.println("Finding approvals for event: " + id);
            List<Approval> approvals = approvalMapper.findByEventId(id);
            System.out.println("Approvals found: " + (approvals != null ? approvals.size() : "null"));

            if (approvals != null && !approvals.isEmpty()) {
                for (Approval approval : approvals) {
                    System.out.println("Deleting approval: " + approval.getId());
                    approvalMapper.deleteById(approval.getId());
                }
            }

            // Then delete the event
            System.out.println("Deleting event: " + id);
            eventMapper.deleteById(id);
            System.out.println("Event deleted successfully");
        } catch (Exception e) {
            System.err.println("ERROR in deleteEvent: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    public EventDto submitEvent(Long id, Long approverId) {
        Event event = eventMapper.findById(id);
        if (event == null) {
            throw new RuntimeException("Event not found: " + id);
        }

        if (event.getStatus() != Event.EventStatus.DRAFT) {
            throw new RuntimeException("Can only submit draft events");
        }

        event.setStatus(Event.EventStatus.SUBMITTED);
        eventMapper.update(event);

        Approval approval = Approval.builder()
                .eventId(event.getId())
                .stepOrder(1)
                .approverId(approverId)
                .submittedAt(LocalDateTime.now())
                .build();
        approvalMapper.insert(approval);

        // 연차 신청 시 승인자에게 메일 발송
        if (event.getEventType() == Event.EventType.LEAVE && event.getMember() != null) {
            try {
                Member approver = memberMapper.findById(approverId);
                if (approver != null && approver.getEmail() != null && !approver.getEmail().isEmpty()) {
                    BigDecimal leaveAmount = event.getLeaveAmount() != null ? event.getLeaveAmount() : BigDecimal.ZERO;
                    Member applicant = event.getMember();

                    // DEBUG: SMTP 비밀번호 확인
                    System.out.println("=== EventService EMAIL DEBUG ===");
                    System.out.println("신청자 이메일: " + applicant.getEmail());
                    System.out.println("신청자 SMTP 비밀번호: " + (applicant.getSmtpPassword() != null ? "있음 (길이: " + applicant.getSmtpPassword().length() + ")" : "NULL"));
                    System.out.println("승인자 이메일: " + approver.getEmail());

                    emailService.sendLeaveApplicationEmail(
                        applicant.getEmail(),
                        applicant.getSmtpPassword(),
                        approver.getEmail(),
                        applicant.getName(),
                        applicant.getEmail(),
                        event.getDepartment(),
                        event.getPosition(),
                        event.getDivision(),
                        applicant.getPhone(),
                        event.getEventSubtype() != null ? event.getEventSubtype() : "휴가",
                        event.getStartDate(),
                        event.getEndDate(),
                        leaveAmount.toString() + "일",
                        event.getDescription()
                    );
                }
            } catch (Exception e) {
                System.err.println("메일 발송 실패: " + e.getMessage());
            }
        }

        return convertToDto(event);
    }

    @Transactional
    public EventDto approveEvent(Long eventId, Long approverId, String comment) {
        Event event = eventMapper.findById(eventId);
        if (event == null) {
            throw new RuntimeException("Event not found: " + eventId);
        }

        if (event.getStatus() != Event.EventStatus.SUBMITTED) {
            throw new RuntimeException("Can only approve submitted events");
        }

        Approval approval = approvalMapper.findByEventIdAndStepOrder(eventId, 1);
        if (approval == null) {
            throw new RuntimeException("Approval not found");
        }

        if (!approval.getApproverId().equals(approverId)) {
            throw new RuntimeException("Only assigned approver can approve");
        }

        approval.setDecision(Approval.ApprovalDecision.APPROVED);
        approval.setComment(comment);
        approval.setDecidedAt(LocalDateTime.now());
        approvalMapper.update(approval);

        event.setStatus(Event.EventStatus.APPROVED);
        eventMapper.update(event);

        return convertToDto(event);
    }

    @Transactional
    public EventDto rejectEvent(Long eventId, Long approverId, String comment) {
        Event event = eventMapper.findById(eventId);
        if (event == null) {
            throw new RuntimeException("Event not found: " + eventId);
        }

        if (event.getStatus() != Event.EventStatus.SUBMITTED) {
            throw new RuntimeException("Can only reject submitted events");
        }

        Approval approval = approvalMapper.findByEventIdAndStepOrder(eventId, 1);
        if (approval == null) {
            throw new RuntimeException("Approval not found");
        }

        if (!approval.getApproverId().equals(approverId)) {
            throw new RuntimeException("Only assigned approver can reject");
        }

        approval.setDecision(Approval.ApprovalDecision.REJECTED);
        approval.setComment(comment);
        approval.setDecidedAt(LocalDateTime.now());
        approvalMapper.update(approval);

        event.setStatus(Event.EventStatus.REJECTED);
        eventMapper.update(event);

        return convertToDto(event);
    }

    @Transactional
    public EventDto requestCancellation(Long eventId, Long approverId) {
        Event event = eventMapper.findById(eventId);
        if (event == null) {
            throw new RuntimeException("Event not found: " + eventId);
        }

        if (event.getStatus() != Event.EventStatus.APPROVED) {
            throw new RuntimeException("Can only request cancellation for approved events");
        }

        event.setStatus(Event.EventStatus.SUBMITTED);
        eventMapper.update(event);

        // Check if cancellation approval already exists
        Approval existingApproval = approvalMapper.findByEventIdAndStepOrder(eventId, 2);
        if (existingApproval == null) {
            // Create new cancellation approval record only if it doesn't exist
            Approval approval = Approval.builder()
                    .eventId(event.getId())
                    .stepOrder(2)
                    .approverId(approverId)
                    .submittedAt(LocalDateTime.now())
                    .build();
            approvalMapper.insert(approval);
        } else {
            // Reset existing approval record for re-approval
            existingApproval.setDecision(null);
            existingApproval.setComment(null);
            existingApproval.setDecidedAt(null);
            existingApproval.setSubmittedAt(LocalDateTime.now());
            approvalMapper.update(existingApproval);
        }

        return convertToDto(event);
    }

    @Transactional
    public EventDto approveCancellation(Long eventId, Long approverId, String comment) {
        Event event = eventMapper.findById(eventId);
        if (event == null) {
            throw new RuntimeException("Event not found: " + eventId);
        }

        Approval approval = approvalMapper.findByEventIdAndStepOrder(eventId, 2);
        if (approval == null) {
            throw new RuntimeException("Cancellation approval not found");
        }

        if (!approval.getApproverId().equals(approverId)) {
            throw new RuntimeException("Only assigned approver can approve cancellation");
        }

        approval.setDecision(Approval.ApprovalDecision.APPROVED);
        approval.setComment(comment != null && comment.trim().isEmpty() ? null : comment);
        approval.setDecidedAt(LocalDateTime.now());
        approvalMapper.update(approval);

        event.setStatus(Event.EventStatus.CANCELED);
        eventMapper.update(event);

        return convertToDto(event);
    }

    @Transactional
    public EventDto rejectCancellation(Long eventId, Long approverId, String comment) {
        Event event = eventMapper.findById(eventId);
        if (event == null) {
            throw new RuntimeException("Event not found: " + eventId);
        }

        Approval approval = approvalMapper.findByEventIdAndStepOrder(eventId, 2);
        if (approval == null) {
            throw new RuntimeException("Cancellation approval not found");
        }

        if (!approval.getApproverId().equals(approverId)) {
            throw new RuntimeException("Only assigned approver can reject cancellation");
        }

        approval.setDecision(Approval.ApprovalDecision.REJECTED);
        approval.setComment(comment != null && comment.trim().isEmpty() ? null : comment);
        approval.setDecidedAt(LocalDateTime.now());
        approvalMapper.update(approval);

        event.setStatus(Event.EventStatus.APPROVED);
        eventMapper.update(event);

        return convertToDto(event);
    }

    private EventDto convertToDto(Event event) {
        // MyBatis가 join을 통해 member 정보를 이미 채워줌
        String memberName = null;
        if (event.getMember() != null) {
            memberName = event.getMember().getName();
        }

        return EventDto.builder()
                .id(event.getId())
                .memberId(event.getMemberId())
                .memberName(memberName)
                .eventType(event.getEventType())
                .eventSubtype(event.getEventSubtype())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .leaveAmount(event.getLeaveAmount())
                .title(event.getTitle())
                .description(event.getDescription())
                .status(event.getStatus())
                .department(event.getDepartment())
                .position(event.getPosition())
                .division(event.getDivision())
                .build();
    }
}
