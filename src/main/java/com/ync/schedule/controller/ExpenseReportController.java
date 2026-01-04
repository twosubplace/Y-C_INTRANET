package com.ync.schedule.controller;

import com.ync.schedule.dto.ExpenseReportDto;
import com.ync.schedule.service.ExpenseReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expense-reports")
@CrossOrigin(origins = "*")
public class ExpenseReportController {

    private final ExpenseReportService expenseReportService;

    public ExpenseReportController(ExpenseReportService expenseReportService) {
        this.expenseReportService = expenseReportService;
    }

    @GetMapping
    public ResponseEntity<List<ExpenseReportDto>> getAllExpenseReports() {
        List<ExpenseReportDto> reports = expenseReportService.getAllExpenseReports();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseReportDto> getExpenseReportById(@PathVariable Long id) {
        ExpenseReportDto report = expenseReportService.getExpenseReportById(id);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ExpenseReportDto>> getExpenseReportsByMemberId(@PathVariable Long memberId) {
        List<ExpenseReportDto> reports = expenseReportService.getExpenseReportsByMemberId(memberId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/month/{reportMonth}")
    public ResponseEntity<List<ExpenseReportDto>> getExpenseReportsByMonth(@PathVariable String reportMonth) {
        LocalDate month = LocalDate.parse(reportMonth + "-01");
        List<ExpenseReportDto> reports = expenseReportService.getExpenseReportsByMonth(month);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ExpenseReportDto>> getExpenseReportsByStatus(@PathVariable String status) {
        List<ExpenseReportDto> reports = expenseReportService.getExpenseReportsByStatus(status);
        return ResponseEntity.ok(reports);
    }

    @PostMapping
    public ResponseEntity<ExpenseReportDto> createExpenseReport(@RequestBody ExpenseReportDto dto) {
        ExpenseReportDto created = expenseReportService.createExpenseReport(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseReportDto> updateExpenseReport(@PathVariable Long id, @RequestBody ExpenseReportDto dto) {
        ExpenseReportDto updated = expenseReportService.updateExpenseReport(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpenseReport(@PathVariable Long id) {
        expenseReportService.deleteExpenseReport(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<ExpenseReportDto> submitExpenseReport(@PathVariable Long id) {
        ExpenseReportDto submitted = expenseReportService.submitExpenseReport(id);
        return ResponseEntity.ok(submitted);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ExpenseReportDto> approveExpenseReport(@PathVariable Long id) {
        ExpenseReportDto approved = expenseReportService.approveExpenseReport(id);
        return ResponseEntity.ok(approved);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ExpenseReportDto> rejectExpenseReport(@PathVariable Long id) {
        ExpenseReportDto rejected = expenseReportService.rejectExpenseReport(id);
        return ResponseEntity.ok(rejected);
    }
}
