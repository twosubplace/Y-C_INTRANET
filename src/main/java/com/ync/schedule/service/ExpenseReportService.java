package com.ync.schedule.service;

import com.ync.schedule.domain.ExpenseReport;
import com.ync.schedule.domain.Member;
import com.ync.schedule.dto.ExpenseReportDto;
import com.ync.schedule.mapper.ExpenseReportMapper;
import com.ync.schedule.mapper.MemberMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ExpenseReportService {

    private final ExpenseReportMapper expenseReportMapper;
    private final MemberMapper memberMapper;

    public ExpenseReportService(ExpenseReportMapper expenseReportMapper, MemberMapper memberMapper) {
        this.expenseReportMapper = expenseReportMapper;
        this.memberMapper = memberMapper;
    }

    public List<ExpenseReportDto> getAllExpenseReports() {
        return expenseReportMapper.findAll();
    }

    public ExpenseReportDto getExpenseReportById(Long id) {
        ExpenseReport report = expenseReportMapper.findById(id);
        if (report == null) {
            throw new RuntimeException("Expense report not found: " + id);
        }
        return convertToDto(report);
    }

    public List<ExpenseReportDto> getExpenseReportsByMemberId(Long memberId) {
        return expenseReportMapper.findByMemberId(memberId);
    }

    public List<ExpenseReportDto> getExpenseReportsByMonth(LocalDate reportMonth) {
        return expenseReportMapper.findByMonth(reportMonth);
    }

    public List<ExpenseReportDto> getExpenseReportsByStatus(String status) {
        return expenseReportMapper.findByStatus(status);
    }

    @Transactional
    public ExpenseReportDto createExpenseReport(ExpenseReportDto dto) {
        Member member = memberMapper.findById(dto.getMemberId());
        if (member == null) {
            throw new RuntimeException("Member not found: " + dto.getMemberId());
        }

        ExpenseReport report = ExpenseReport.builder()
                .memberId(dto.getMemberId())
                .title(dto.getTitle())
                .reportMonth(dto.getReportMonth())
                .totalAmount(dto.getTotalAmount())
                .description(dto.getDescription())
                .status(dto.getStatus() != null ? dto.getStatus() : ExpenseReport.ExpenseStatus.DRAFT)
                .filePath(dto.getFilePath())
                .build();

        expenseReportMapper.insert(report);
        return convertToDto(report);
    }

    @Transactional
    public ExpenseReportDto updateExpenseReport(Long id, ExpenseReportDto dto) {
        ExpenseReport report = expenseReportMapper.findById(id);
        if (report == null) {
            throw new RuntimeException("Expense report not found: " + id);
        }

        report.setMemberId(dto.getMemberId());
        report.setTitle(dto.getTitle());
        report.setReportMonth(dto.getReportMonth());
        report.setTotalAmount(dto.getTotalAmount());
        report.setDescription(dto.getDescription());
        report.setStatus(dto.getStatus());
        report.setFilePath(dto.getFilePath());

        expenseReportMapper.update(report);
        return convertToDto(report);
    }

    @Transactional
    public void deleteExpenseReport(Long id) {
        ExpenseReport report = expenseReportMapper.findById(id);
        if (report == null) {
            throw new RuntimeException("Expense report not found: " + id);
        }

        expenseReportMapper.deleteById(id);
    }

    @Transactional
    public ExpenseReportDto submitExpenseReport(Long id) {
        ExpenseReport report = expenseReportMapper.findById(id);
        if (report == null) {
            throw new RuntimeException("Expense report not found: " + id);
        }

        if (report.getStatus() != ExpenseReport.ExpenseStatus.DRAFT) {
            throw new RuntimeException("Can only submit draft reports");
        }

        report.setStatus(ExpenseReport.ExpenseStatus.SUBMITTED);
        expenseReportMapper.update(report);

        return convertToDto(report);
    }

    @Transactional
    public ExpenseReportDto approveExpenseReport(Long id) {
        ExpenseReport report = expenseReportMapper.findById(id);
        if (report == null) {
            throw new RuntimeException("Expense report not found: " + id);
        }

        if (report.getStatus() != ExpenseReport.ExpenseStatus.SUBMITTED) {
            throw new RuntimeException("Can only approve submitted reports");
        }

        report.setStatus(ExpenseReport.ExpenseStatus.APPROVED);
        expenseReportMapper.update(report);

        return convertToDto(report);
    }

    @Transactional
    public ExpenseReportDto rejectExpenseReport(Long id) {
        ExpenseReport report = expenseReportMapper.findById(id);
        if (report == null) {
            throw new RuntimeException("Expense report not found: " + id);
        }

        if (report.getStatus() != ExpenseReport.ExpenseStatus.SUBMITTED) {
            throw new RuntimeException("Can only reject submitted reports");
        }

        report.setStatus(ExpenseReport.ExpenseStatus.REJECTED);
        expenseReportMapper.update(report);

        return convertToDto(report);
    }

    private ExpenseReportDto convertToDto(ExpenseReport report) {
        String memberName = null;
        String departmentName = null;

        if (report.getMember() != null) {
            memberName = report.getMember().getName();
            if (report.getMember().getDepartment() != null) {
                departmentName = report.getMember().getDepartment();
            }
        }

        return ExpenseReportDto.builder()
                .id(report.getId())
                .memberId(report.getMemberId())
                .memberName(memberName)
                .departmentName(departmentName)
                .title(report.getTitle())
                .reportMonth(report.getReportMonth())
                .totalAmount(report.getTotalAmount())
                .description(report.getDescription())
                .status(report.getStatus())
                .filePath(report.getFilePath())
                .createdAt(report.getCreatedAt())
                .updatedAt(report.getUpdatedAt())
                .build();
    }
}
