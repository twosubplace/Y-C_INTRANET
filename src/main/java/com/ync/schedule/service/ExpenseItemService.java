package com.ync.schedule.service;

import com.ync.schedule.domain.ExpenseItem;
import com.ync.schedule.domain.Member;
import com.ync.schedule.dto.ExpenseItemDto;
import com.ync.schedule.dto.WelfareSummaryDto;
import com.ync.schedule.dto.WelfareUsageDto;
import com.ync.schedule.mapper.ExpenseItemMapper;
import com.ync.schedule.mapper.MemberMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ExpenseItemService {

    private final ExpenseItemMapper expenseItemMapper;
    private final MemberMapper memberMapper;

    public ExpenseItemService(ExpenseItemMapper expenseItemMapper, MemberMapper memberMapper) {
        this.expenseItemMapper = expenseItemMapper;
        this.memberMapper = memberMapper;
    }

    public List<ExpenseItemDto> getAllExpenseItems() {
        return expenseItemMapper.findAll();
    }

    public ExpenseItemDto getExpenseItemById(Long id) {
        ExpenseItem item = expenseItemMapper.findById(id);
        if (item == null) {
            throw new RuntimeException("Expense item not found: " + id);
        }
        return convertToDto(item);
    }

    public List<ExpenseItemDto> getExpenseItemsByMemberId(Long memberId) {
        return expenseItemMapper.findByMemberId(memberId);
    }

    public List<ExpenseItemDto> getExpenseItemsByDateRange(LocalDate startDate, LocalDate endDate) {
        return expenseItemMapper.findByDateRange(startDate, endDate);
    }

    public List<ExpenseItemDto> getExpenseItemsByMemberIdAndDateRange(Long memberId, LocalDate startDate, LocalDate endDate) {
        return expenseItemMapper.findByMemberIdAndDateRange(memberId, startDate, endDate);
    }

    @Transactional
    public ExpenseItemDto createExpenseItem(ExpenseItemDto dto) {
        Member member = memberMapper.findById(dto.getMemberId());
        if (member == null) {
            throw new RuntimeException("Member not found: " + dto.getMemberId());
        }

        ExpenseItem item = ExpenseItem.builder()
                .memberId(dto.getMemberId())
                .usageDate(dto.getUsageDate())
                .description(dto.getDescription())
                .account(dto.getAccount())
                .amount(dto.getAmount())
                .vendor(dto.getVendor())
                .costCode(dto.getCostCode())
                .projectCode(dto.getProjectCode())
                .note(dto.getNote())
                .welfareFlag(dto.getWelfareFlag())
                .build();

        expenseItemMapper.insert(item);
        return convertToDto(item);
    }

    @Transactional
    public ExpenseItemDto updateExpenseItem(Long id, ExpenseItemDto dto) {
        ExpenseItem item = expenseItemMapper.findById(id);
        if (item == null) {
            throw new RuntimeException("Expense item not found: " + id);
        }

        item.setMemberId(dto.getMemberId());
        item.setUsageDate(dto.getUsageDate());
        item.setDescription(dto.getDescription());
        item.setAccount(dto.getAccount());
        item.setAmount(dto.getAmount());
        item.setVendor(dto.getVendor());
        item.setCostCode(dto.getCostCode());
        item.setProjectCode(dto.getProjectCode());
        item.setNote(dto.getNote());
        item.setWelfareFlag(dto.getWelfareFlag());

        expenseItemMapper.update(item);
        return convertToDto(item);
    }

    @Transactional
    public void deleteExpenseItem(Long id) {
        ExpenseItem item = expenseItemMapper.findById(id);
        if (item == null) {
            throw new RuntimeException("Expense item not found: " + id);
        }

        expenseItemMapper.deleteById(id);
    }

    public WelfareSummaryDto getWelfareUsageSummary(Long memberId, Integer year) {
        Member member = memberMapper.findById(memberId);
        if (member == null) {
            throw new RuntimeException("Member not found: " + memberId);
        }

        List<ExpenseItemDto> welfareExpenses = expenseItemMapper.findWelfareExpensesByMemberIdAndYear(memberId, year);

        // Quarter budget: 40만원 per quarter
        BigDecimal quarterBudget = new BigDecimal("400000");
        BigDecimal annualBudget = quarterBudget.multiply(new BigDecimal("4")); // 160만원

        // Calculate total welfare usage for the year
        BigDecimal totalUsed = BigDecimal.ZERO;
        for (ExpenseItemDto expense : welfareExpenses) {
            totalUsed = totalUsed.add(expense.getAmount());
        }

        // Allocate usage from Q1 -> Q2 -> Q3 -> Q4 sequentially
        List<WelfareUsageDto> quarters = new ArrayList<>();
        BigDecimal remainingToAllocate = totalUsed;

        for (int quarter = 1; quarter <= 4; quarter++) {
            BigDecimal quarterUsed = BigDecimal.ZERO;

            if (remainingToAllocate.compareTo(BigDecimal.ZERO) > 0) {
                if (remainingToAllocate.compareTo(quarterBudget) >= 0) {
                    // Use full quarter budget
                    quarterUsed = quarterBudget;
                    remainingToAllocate = remainingToAllocate.subtract(quarterBudget);
                } else {
                    // Use remaining amount
                    quarterUsed = remainingToAllocate;
                    remainingToAllocate = BigDecimal.ZERO;
                }
            }

            BigDecimal quarterRemaining = quarterBudget.subtract(quarterUsed);

            WelfareUsageDto quarterDto = WelfareUsageDto.builder()
                    .year(year)
                    .quarter(quarter)
                    .budget(quarterBudget)
                    .used(quarterUsed)
                    .remaining(quarterRemaining)
                    .build();
            quarters.add(quarterDto);
        }

        BigDecimal annualRemaining = annualBudget.subtract(totalUsed);

        return WelfareSummaryDto.builder()
                .memberId(memberId)
                .memberName(member.getName())
                .year(year)
                .quarters(quarters)
                .annualBudget(annualBudget)
                .annualUsed(totalUsed)
                .annualRemaining(annualRemaining)
                .build();
    }

    private ExpenseItemDto convertToDto(ExpenseItem item) {
        String memberName = null;
        String departmentName = null;

        if (item.getMember() != null) {
            memberName = item.getMember().getName();
            if (item.getMember().getDepartment() != null) {
                departmentName = item.getMember().getDepartment();
            }
        }

        return ExpenseItemDto.builder()
                .id(item.getId())
                .memberId(item.getMemberId())
                .memberName(memberName)
                .departmentName(departmentName)
                .usageDate(item.getUsageDate())
                .description(item.getDescription())
                .account(item.getAccount())
                .amount(item.getAmount())
                .vendor(item.getVendor())
                .costCode(item.getCostCode())
                .projectCode(item.getProjectCode())
                .note(item.getNote())
                .welfareFlag(item.getWelfareFlag())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }
}
