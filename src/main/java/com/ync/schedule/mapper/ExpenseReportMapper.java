package com.ync.schedule.mapper;

import com.ync.schedule.domain.ExpenseReport;
import com.ync.schedule.dto.ExpenseReportDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ExpenseReportMapper {

    List<ExpenseReportDto> findAll();

    ExpenseReport findById(@Param("id") Long id);

    List<ExpenseReportDto> findByMemberId(@Param("memberId") Long memberId);

    List<ExpenseReportDto> findByMonth(@Param("reportMonth") LocalDate reportMonth);

    List<ExpenseReportDto> findByStatus(@Param("status") String status);

    int insert(ExpenseReport expenseReport);

    int update(ExpenseReport expenseReport);

    int deleteById(@Param("id") Long id);
}
