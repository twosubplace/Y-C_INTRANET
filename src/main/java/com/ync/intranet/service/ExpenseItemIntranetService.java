package com.ync.intranet.service;

import com.ync.intranet.domain.ExpenseItemIntranet;
import com.ync.intranet.domain.MemberIntranet;
import com.ync.intranet.dto.WelfareSummaryDto;
import com.ync.intranet.dto.WelfareUsageDto;
import com.ync.intranet.mapper.ExpenseItemIntranetMapper;
import com.ync.intranet.mapper.ExpenseReportIntranetMapper;
import com.ync.intranet.mapper.MemberIntranetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 경비 항목 서비스 (인트라넷)
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenseItemIntranetService {

    private final ExpenseItemIntranetMapper expenseItemMapper;
    private final ExpenseReportIntranetMapper expenseReportMapper;
    private final MemberIntranetMapper memberMapper;

    /**
     * 경비 항목 조회 (ID)
     */
    public ExpenseItemIntranet getExpenseItemById(Long id) {
        return expenseItemMapper.findById(id);
    }

    /**
     * 경비보고서의 모든 항목 조회
     */
    public List<ExpenseItemIntranet> getExpenseItemsByReportId(Long expenseReportId) {
        return expenseItemMapper.findByExpenseReportId(expenseReportId);
    }

    /**
     * 카테고리별 항목 조회
     */
    public List<ExpenseItemIntranet> getExpenseItemsByCategory(String category) {
        return expenseItemMapper.findByCategory(category);
    }

    /**
     * 전체 경비 항목 조회
     */
    public List<ExpenseItemIntranet> getAllExpenseItems() {
        return expenseItemMapper.findAll();
    }

    /**
     * 경비 항목 생성
     */
    @Transactional
    public ExpenseItemIntranet createExpenseItem(ExpenseItemIntranet expenseItem) {
        expenseItemMapper.insert(expenseItem);

        // 경비보고서의 총 금액 업데이트 (expenseReportId가 있을 경우만)
        if (expenseItem.getExpenseReportId() != null) {
            updateReportTotalAmount(expenseItem.getExpenseReportId());
        }

        return expenseItem;
    }

    /**
     * 경비 항목 일괄 생성
     */
    @Transactional
    public void createExpenseItems(List<ExpenseItemIntranet> expenseItems) {
        if (expenseItems == null || expenseItems.isEmpty()) {
            return;
        }

        expenseItemMapper.insertBatch(expenseItems);

        // 경비보고서의 총 금액 업데이트
        if (!expenseItems.isEmpty()) {
            updateReportTotalAmount(expenseItems.get(0).getExpenseReportId());
        }
    }

    /**
     * 경비 항목 수정
     */
    @Transactional
    public ExpenseItemIntranet updateExpenseItem(Long id, ExpenseItemIntranet expenseItem) {
        ExpenseItemIntranet existing = expenseItemMapper.findById(id);
        if (existing == null) {
            throw new RuntimeException("경비 항목을 찾을 수 없습니다: " + id);
        }

        existing.setMemberId(expenseItem.getMemberId());
        existing.setUsageDate(expenseItem.getUsageDate());
        existing.setDescription(expenseItem.getDescription());
        existing.setAccount(expenseItem.getAccount());
        existing.setAmount(expenseItem.getAmount());
        existing.setVendor(expenseItem.getVendor());
        existing.setCostCode(expenseItem.getCostCode());
        existing.setProjectCode(expenseItem.getProjectCode());
        existing.setNote(expenseItem.getNote());
        existing.setWelfareFlag(expenseItem.getWelfareFlag());

        expenseItemMapper.update(existing);

        // 경비보고서의 총 금액 업데이트
        updateReportTotalAmount(existing.getExpenseReportId());

        return existing;
    }

    /**
     * 경비 항목 삭제
     */
    @Transactional
    public void deleteExpenseItem(Long id) {
        ExpenseItemIntranet expenseItem = expenseItemMapper.findById(id);
        if (expenseItem == null) {
            throw new RuntimeException("경비 항목을 찾을 수 없습니다: " + id);
        }

        Long expenseReportId = expenseItem.getExpenseReportId();
        expenseItemMapper.deleteById(id);

        // 경비보고서의 총 금액 업데이트
        updateReportTotalAmount(expenseReportId);
    }

    /**
     * 경비보고서의 모든 항목 삭제
     */
    @Transactional
    public void deleteAllExpenseItems(Long expenseReportId) {
        expenseItemMapper.deleteByExpenseReportId(expenseReportId);

        // 경비보고서의 총 금액을 0으로 업데이트
        expenseReportMapper.updateTotalAmount(expenseReportId, BigDecimal.ZERO);
    }

    /**
     * 경비보고서의 총 금액 재계산 및 업데이트
     */
    private void updateReportTotalAmount(Long expenseReportId) {
        if (expenseReportId == null) {
            return; // expenseReportId가 null이면 업데이트하지 않음
        }

        List<ExpenseItemIntranet> items = expenseItemMapper.findByExpenseReportId(expenseReportId);

        BigDecimal totalAmount = items.stream()
                .map(ExpenseItemIntranet::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        expenseReportMapper.updateTotalAmount(expenseReportId, totalAmount);
    }

    /**
     * 복지비 사용 현황 조회
     */
    public WelfareSummaryDto getWelfareUsageSummary(Long memberId, Integer year) {
        MemberIntranet member = memberMapper.findById(memberId);
        if (member == null) {
            throw new RuntimeException("멤버를 찾을 수 없습니다: " + memberId);
        }

        List<ExpenseItemIntranet> welfareExpenses = expenseItemMapper.findWelfareExpensesByMemberIdAndYear(memberId, year);

        // 분기당 예산: 40만원
        BigDecimal quarterBudget = new BigDecimal("400000");
        BigDecimal annualBudget = quarterBudget.multiply(new BigDecimal("4")); // 160만원

        // 연간 총 복지비 사용액 계산
        BigDecimal totalUsed = BigDecimal.ZERO;
        for (ExpenseItemIntranet expense : welfareExpenses) {
            totalUsed = totalUsed.add(expense.getAmount());
        }

        // Q1 -> Q2 -> Q3 -> Q4 순서로 사용액 할당
        List<WelfareUsageDto> quarters = new ArrayList<>();
        BigDecimal remainingToAllocate = totalUsed;

        for (int quarter = 1; quarter <= 4; quarter++) {
            BigDecimal quarterUsed = BigDecimal.ZERO;

            if (remainingToAllocate.compareTo(BigDecimal.ZERO) > 0) {
                if (remainingToAllocate.compareTo(quarterBudget) >= 0) {
                    quarterUsed = quarterBudget;
                    remainingToAllocate = remainingToAllocate.subtract(quarterBudget);
                } else {
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
}
