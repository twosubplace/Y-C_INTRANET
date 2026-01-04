package com.ync.schedule.controller;

import com.ync.schedule.dto.ExcelDownloadRequest;
import com.ync.schedule.dto.ExpenseItemDto;
import com.ync.schedule.dto.WelfareSummaryDto;
import com.ync.schedule.service.ExpenseExcelService;
import com.ync.schedule.service.ExpenseItemService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expense-items")
@CrossOrigin(origins = "*")
public class ExpenseItemController {

    private final ExpenseItemService expenseItemService;
    private final ExpenseExcelService expenseExcelService;

    public ExpenseItemController(ExpenseItemService expenseItemService, ExpenseExcelService expenseExcelService) {
        this.expenseItemService = expenseItemService;
        this.expenseExcelService = expenseExcelService;
    }

    @GetMapping
    public ResponseEntity<List<ExpenseItemDto>> getAllExpenseItems() {
        List<ExpenseItemDto> items = expenseItemService.getAllExpenseItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseItemDto> getExpenseItemById(@PathVariable Long id) {
        ExpenseItemDto item = expenseItemService.getExpenseItemById(id);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ExpenseItemDto>> getExpenseItemsByMemberId(@PathVariable Long memberId) {
        List<ExpenseItemDto> items = expenseItemService.getExpenseItemsByMemberId(memberId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<ExpenseItemDto>> getExpenseItemsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<ExpenseItemDto> items = expenseItemService.getExpenseItemsByDateRange(start, end);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/member/{memberId}/date-range")
    public ResponseEntity<List<ExpenseItemDto>> getExpenseItemsByMemberIdAndDateRange(
            @PathVariable Long memberId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<ExpenseItemDto> items = expenseItemService.getExpenseItemsByMemberIdAndDateRange(memberId, start, end);
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<ExpenseItemDto> createExpenseItem(@RequestBody ExpenseItemDto dto) {
        ExpenseItemDto created = expenseItemService.createExpenseItem(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseItemDto> updateExpenseItem(@PathVariable Long id, @RequestBody ExpenseItemDto dto) {
        ExpenseItemDto updated = expenseItemService.updateExpenseItem(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpenseItem(@PathVariable Long id) {
        expenseItemService.deleteExpenseItem(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/welfare-usage/{memberId}/{year}")
    public ResponseEntity<WelfareSummaryDto> getWelfareUsage(
            @PathVariable Long memberId,
            @PathVariable Integer year) {
        WelfareSummaryDto summary = expenseItemService.getWelfareUsageSummary(memberId, year);
        return ResponseEntity.ok(summary);
    }

    @PostMapping("/excel")
    public ResponseEntity<byte[]> downloadExcel(@RequestBody ExcelDownloadRequest request) {
        try {
            System.out.println("Excel download request received: " + request);
            byte[] excelData = expenseExcelService.generateExcel(request);

            // 파일명 생성 (한글 인코딩)
            String memberName = (request.getMember() != null) ? request.getMember() : "unknown";
            String yearMonth = (request.getYearMonth() != null) ? request.getYearMonth() : "unknown";
            String fileName = String.format("expense_report_%s_%s.xlsx", memberName, yearMonth);
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename*=UTF-8''" + encodedFileName);
            headers.setContentLength(excelData.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Excel download error: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
