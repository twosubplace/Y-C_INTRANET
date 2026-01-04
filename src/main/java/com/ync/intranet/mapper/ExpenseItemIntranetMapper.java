package com.ync.intranet.mapper;

import com.ync.intranet.domain.ExpenseItemIntranet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 경비 항목 Mapper (인트라넷)
 */
@Mapper
public interface ExpenseItemIntranetMapper {

    /**
     * ID로 경비 항목 조회
     */
    ExpenseItemIntranet findById(@Param("id") Long id);

    /**
     * 경비보고서 ID로 항목 조회
     */
    List<ExpenseItemIntranet> findByExpenseReportId(@Param("expenseReportId") Long expenseReportId);

    /**
     * 카테고리별 항목 조회
     */
    List<ExpenseItemIntranet> findByCategory(@Param("category") String category);

    /**
     * 전체 경비 항목 조회
     */
    List<ExpenseItemIntranet> findAll();

    /**
     * 경비 항목 등록
     */
    void insert(ExpenseItemIntranet expenseItem);

    /**
     * 경비 항목 일괄 등록
     */
    void insertBatch(@Param("items") List<ExpenseItemIntranet> items);

    /**
     * 경비 항목 수정
     */
    void update(ExpenseItemIntranet expenseItem);

    /**
     * 경비 항목 삭제
     */
    void deleteById(@Param("id") Long id);

    /**
     * 경비보고서의 모든 항목 삭제
     */
    void deleteByExpenseReportId(@Param("expenseReportId") Long expenseReportId);

    /**
     * 멤버 ID와 연도로 복지비 항목 조회
     */
    List<ExpenseItemIntranet> findWelfareExpensesByMemberIdAndYear(@Param("memberId") Long memberId, @Param("year") Integer year);
}
