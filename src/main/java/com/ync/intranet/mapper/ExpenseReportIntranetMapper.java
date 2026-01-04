package com.ync.intranet.mapper;

import com.ync.intranet.domain.ExpenseReportIntranet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 경비보고서 Mapper (인트라넷)
 */
@Mapper
public interface ExpenseReportIntranetMapper {

    /**
     * ID로 경비보고서 조회
     */
    ExpenseReportIntranet findById(@Param("id") Long id);

    /**
     * 문서 ID로 경비보고서 조회
     */
    ExpenseReportIntranet findByDocumentId(@Param("documentId") Long documentId);

    /**
     * 보고 월별 경비보고서 조회
     */
    List<ExpenseReportIntranet> findByReportMonth(@Param("reportMonth") LocalDate reportMonth);

    /**
     * 전체 경비보고서 조회
     */
    List<ExpenseReportIntranet> findAll();

    /**
     * 경비보고서 등록
     */
    void insert(ExpenseReportIntranet expenseReport);

    /**
     * 경비보고서 수정
     */
    void update(ExpenseReportIntranet expenseReport);

    /**
     * 총 금액 업데이트
     */
    void updateTotalAmount(@Param("id") Long id, @Param("totalAmount") java.math.BigDecimal totalAmount);

    /**
     * 경비보고서 삭제
     */
    void deleteById(@Param("id") Long id);

    /**
     * 문서 ID로 경비보고서 삭제
     */
    void deleteByDocumentId(@Param("documentId") Long documentId);
}
