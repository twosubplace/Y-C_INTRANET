package com.ync.intranet.service;

import com.ync.intranet.dto.ExcelDownloadRequest;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service("expenseExcelIntranetService")
public class ExpenseExcelService {

    private static final Logger log = LoggerFactory.getLogger(ExpenseExcelService.class);

    private static final String TEMPLATE_PATH = "template/expense_report_template.xlsx";

    /**
     * 템플릿 기반 엑셀 파일 생성
     */
    public byte[] generateExcel(ExcelDownloadRequest request) throws Exception {
        log.info("Starting Excel generation for member: {}, dept: {}, month: {}",
            request.getMember(), request.getDept(), request.getYearMonth());

        // 템플릿 파일 로드
        ClassPathResource resource = new ClassPathResource(TEMPLATE_PATH);
        if (!resource.exists()) {
            log.error("Template file not found at: {}", TEMPLATE_PATH);
            throw new RuntimeException("Template file not found: " + TEMPLATE_PATH);
        }

        log.info("Template file found, loading...");

        try (InputStream templateStream = resource.getInputStream();
             Workbook workbook = WorkbookFactory.create(templateStream)) {

            log.info("Workbook created successfully");
            Sheet sheet = workbook.getSheetAt(0);
            log.info("Sheet retrieved: {}", sheet.getSheetName());

            // 시트 이름 변경: 지출내역(매니저명)
            String memberName = (request.getMember() != null ? request.getMember() : "전체");
            String sheetName1 = "지출내역(" + memberName + ")";
            workbook.setSheetName(0, sheetName1);
            log.info("First sheet name changed to: {}", sheetName1);

            // 두 번째 시트가 있으면 이름 변경: 지출보고서(매니저명)
            if (workbook.getNumberOfSheets() > 1) {
                String sheetName2 = "지출보고서(" + memberName + ")";
                workbook.setSheetName(1, sheetName2);
                log.info("Second sheet name changed to: {}", sheetName2);
            }

            // 1. 헤더 정보 입력 (Name, Department, Approval Date)
            fillHeaderInfo(sheet, request);

            // 2. 지출 내역 테이블 찾기 및 데이터 입력
            fillExpenseItems(sheet, request);

            // 3. 수식 강제 재계산 설정
            workbook.setForceFormulaRecalculation(true);

            // 4. ByteArray로 변환하여 반환
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            byte[] result = outputStream.toByteArray();
            log.info("Excel file generated successfully, size: {} bytes", result.length);
            return result;
        } catch (Exception e) {
            log.error("Error generating Excel: ", e);
            throw e;
        }
    }

    /**
     * 헤더 정보 입력 (Name, Department, Approval Date, Parent Department)
     */
    private void fillHeaderInfo(Sheet sheet, ExcelDownloadRequest request) {
        // Approval Date 계산 (yearMonth의 마지막 날)
        YearMonth ym = YearMonth.parse(request.getYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM"));
        LocalDate approvalDate = ym.atEndOfMonth();
        String approvalDateStr = approvalDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 동적으로 라벨 찾아서 오른쪽 셀에 값 입력
        for (Row row : sheet) {
            if (row == null) continue;

            for (Cell cell : row) {
                if (cell == null || cell.getCellType() != CellType.STRING) continue;

                String cellValue = cell.getStringCellValue().trim();

                // Name 찾기
                if ("Name".equalsIgnoreCase(cellValue) || "이름".equals(cellValue) || "성명".equals(cellValue)) {
                    Cell valueCell = row.getCell(cell.getColumnIndex() + 1);
                    if (valueCell == null) {
                        valueCell = row.createCell(cell.getColumnIndex() + 1);
                    }
                    valueCell.setCellValue(request.getMember());
                    log.info("Set Name: {} at row {}, col {}", request.getMember(), row.getRowNum(), valueCell.getColumnIndex());
                }

                // Position 찾기
                if ("Position".equalsIgnoreCase(cellValue) || "직급".equals(cellValue) || "직위".equals(cellValue) || "직책".equals(cellValue)) {
                    Cell valueCell = row.getCell(cell.getColumnIndex() + 1);
                    if (valueCell == null) {
                        valueCell = row.createCell(cell.getColumnIndex() + 1);
                    }
                    String position = request.getPosition() != null ? request.getPosition() : "";
                    valueCell.setCellValue(position);
                    log.info("Set Position: {} at row {}, col {}", position, row.getRowNum(), valueCell.getColumnIndex());
                }

                // Department 찾기
                if ("Department".equalsIgnoreCase(cellValue) || "부서".equals(cellValue) || "소속".equals(cellValue)) {
                    Cell valueCell = row.getCell(cell.getColumnIndex() + 1);
                    if (valueCell == null) {
                        valueCell = row.createCell(cell.getColumnIndex() + 1);
                    }
                    valueCell.setCellValue(request.getDept());
                    log.info("Set Department: {} at row {}, col {}", request.getDept(), row.getRowNum(), valueCell.getColumnIndex());
                }

                // Parent Department (사업부서) 찾기
                if ("사업부서".equals(cellValue) || "Parent Department".equalsIgnoreCase(cellValue) || "본부".equals(cellValue)) {
                    Cell valueCell = row.getCell(cell.getColumnIndex() + 1);
                    if (valueCell == null) {
                        valueCell = row.createCell(cell.getColumnIndex() + 1);
                    }
                    // items의 첫 번째 항목에서 parentDeptName 가져오기
                    String parentDeptName = "전체";
                    if (request.getItems() != null && !request.getItems().isEmpty()) {
                        Object parentDept = request.getItems().get(0).get("parentDeptName");
                        if (parentDept != null) {
                            parentDeptName = parentDept.toString();
                        }
                    }
                    valueCell.setCellValue(parentDeptName);
                    log.info("Set Parent Department: {} at row {}, col {}", parentDeptName, row.getRowNum(), valueCell.getColumnIndex());
                }

                // Approval Date 찾기
                if ("Approval Date".equalsIgnoreCase(cellValue) || "승인일자".equals(cellValue) || "결재일".equals(cellValue)) {
                    Cell valueCell = row.getCell(cell.getColumnIndex() + 1);
                    if (valueCell == null) {
                        valueCell = row.createCell(cell.getColumnIndex() + 1);
                    }
                    valueCell.setCellValue(approvalDateStr);
                    log.info("Set Approval Date: {} at row {}, col {}", approvalDateStr, row.getRowNum(), valueCell.getColumnIndex());
                }
            }
        }
    }

    /**
     * 지출 내역 테이블 찾기 및 데이터 입력
     */
    private void fillExpenseItems(Sheet sheet, ExcelDownloadRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            return;
        }

        // 1. 헤더 행 찾기
        int headerRowIndex = -1;
        Row headerRow = null;
        Map<String, Integer> headerMap = new HashMap<>();

        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            // "Usage Date", "사용일자", "Amount", "금액", "Description", "사용 내용" 등의 헤더가 있는지 확인
            boolean hasUsageDate = false;
            boolean hasAmount = false;

            for (Cell cell : row) {
                if (cell == null || cell.getCellType() != CellType.STRING) continue;
                String cellValue = cell.getStringCellValue().trim();

                if ("Usage Date".equalsIgnoreCase(cellValue) || "사용일자".equals(cellValue) || "사용일시".equals(cellValue)) {
                    hasUsageDate = true;
                }
                if ("Amount".equalsIgnoreCase(cellValue) || "금액".equals(cellValue) || "사용금액".equals(cellValue)) {
                    hasAmount = true;
                }
            }

            if (hasUsageDate && hasAmount) {
                headerRowIndex = i;
                headerRow = row;
                log.info("Found header row at index: {}", headerRowIndex);
                break;
            }
        }

        if (headerRow == null) {
            log.warn("Header row not found in template");
            return;
        }

        // 2. 헤더명과 컬럼 인덱스 매핑
        log.info("=== All cells in header row (including empty) ===");
        int lastCellNum = headerRow.getLastCellNum();
        for (int i = 0; i < lastCellNum; i++) {
            Cell cell = headerRow.getCell(i);
            String cellValue = "";
            if (cell != null && cell.getCellType() == CellType.STRING) {
                cellValue = cell.getStringCellValue().trim();
            }
            log.info("  Index {} ({}): [{}]", i, getExcelColumnName(i), cellValue);

            // 빈 문자열이 아닌 경우에만 헤더맵에 추가
            if (!cellValue.isEmpty()) {
                headerMap.put(cellValue, i);
            }
        }

        log.info("Header map: {}", headerMap);

        // 3. 마지막 템플릿 행의 스타일 가져오기 (포맷 유지를 위해)
        int templateDataRowIndex = headerRowIndex + 1;
        Row templateDataRow = sheet.getRow(templateDataRowIndex);
        if (templateDataRow == null) {
            templateDataRow = sheet.createRow(templateDataRowIndex);
        }

        // 4. 데이터 행 추가
        int currentRowIndex = headerRowIndex + 1;
        int totalCount = request.getItems().size();  // 총 라인 수

        // 증빙서류(영수증) 컬럼 인덱스 미리 찾기
        Integer receiptColIndex = findColumnIndex(headerMap, "receipt");
        if (receiptColIndex == null) {
            // "증빙서류", "영수증" 등 다른 이름으로도 찾기
            for (Map.Entry<String, Integer> header : headerMap.entrySet()) {
                String headerName = header.getKey();
                if (headerName.contains("증빙") || headerName.contains("영수증") ||
                    headerName.equalsIgnoreCase("Receipt")) {
                    receiptColIndex = header.getValue();
                    break;
                }
            }
        }

        for (Map<String, Object> item : request.getItems()) {
            Row dataRow = sheet.getRow(currentRowIndex);
            if (dataRow == null) {
                dataRow = sheet.createRow(currentRowIndex);
            }

            log.info("Processing item: {}", item);

            // 증빙서류(영수증) 컬럼에 총 라인 수 입력
            if (receiptColIndex != null) {
                Cell receiptCell = dataRow.getCell(receiptColIndex);
                if (receiptCell == null) {
                    receiptCell = dataRow.createCell(receiptColIndex);
                }

                // 템플릿 스타일 복사
                Cell templateCell = templateDataRow.getCell(receiptColIndex);
                if (templateCell != null && templateCell.getCellStyle() != null) {
                    receiptCell.setCellStyle(templateCell.getCellStyle());
                }

                receiptCell.setCellValue(totalCount);
            }

            // 각 헤더에 맞춰 값 입력
            for (Map.Entry<String, Object> entry : item.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                // 헤더명과 매칭되는 컬럼 찾기
                Integer colIndex = findColumnIndex(headerMap, key);
                log.info("Key: {}, Value: {}, ColIndex: {}", key, value, colIndex);
                if (colIndex != null) {
                    Cell cell = dataRow.getCell(colIndex);
                    if (cell == null) {
                        cell = dataRow.createCell(colIndex);
                    }

                    // 템플릿 행의 스타일 복사
                    Cell templateCell = templateDataRow.getCell(colIndex);
                    if (templateCell != null && templateCell.getCellStyle() != null) {
                        cell.setCellStyle(templateCell.getCellStyle());
                    }

                    // 값 입력
                    setCellValue(cell, value);
                }
            }

            currentRowIndex++;
        }

        log.info("Inserted {} expense items", request.getItems().size());
    }

    /**
     * 헤더명으로 컬럼 인덱스 찾기 (유연한 매칭)
     */
    private Integer findColumnIndex(Map<String, Integer> headerMap, String key) {
        // 강제 컬럼 매핑 (하드코딩 우선순위)
        Map<String, Integer> forcedMapping = new HashMap<>();
        forcedMapping.put("usageDate", 2);        // C열: 사용일시
        forcedMapping.put("description", 3);      // D열: 사용 내용
        forcedMapping.put("account", 4);          // E열: 계정
        forcedMapping.put("amount", 5);           // F열: 사용금액
        forcedMapping.put("vendor", 6);           // G열: 업소명
        forcedMapping.put("costCode", 7);         // H열: 경비코드
        forcedMapping.put("projectCode", 8);      // I열: 프로젝트코드
        forcedMapping.put("note", 9);             // J열: 비고

        // 강제 매핑에 있으면 그 인덱스 사용
        if (forcedMapping.containsKey(key)) {
            Integer forcedIndex = forcedMapping.get(key);
            log.info("Using forced mapping: key={}, index={}", key, forcedIndex);
            return forcedIndex;
        }

        // 정확히 일치하는 경우
        if (headerMap.containsKey(key)) {
            return headerMap.get(key);
        }

        // 영어-한글 매핑
        Map<String, String[]> aliases = new HashMap<>();
        aliases.put("memberName", new String[]{"Name", "이름", "사용자", "성명"});
        aliases.put("parentDeptName", new String[]{"Parent Department", "본부", "사업부서", "상위부서"});
        aliases.put("deptName", new String[]{"Department", "부서", "팀", "소속"});

        // 별칭으로 매칭
        for (Map.Entry<String, String[]> alias : aliases.entrySet()) {
            if (alias.getKey().equalsIgnoreCase(key)) {
                for (String headerName : alias.getValue()) {
                    if (headerMap.containsKey(headerName)) {
                        return headerMap.get(headerName);
                    }
                }
            }
        }

        return null;
    }

    /**
     * 셀에 값 설정 (타입에 따라 자동 변환)
     */
    private void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }

    /**
     * 컬럼 인덱스를 엑셀 컬럼명으로 변환 (0 -> A, 1 -> B, 4 -> E, etc.)
     */
    private String getExcelColumnName(int columnIndex) {
        StringBuilder columnName = new StringBuilder();
        while (columnIndex >= 0) {
            columnName.insert(0, (char) ('A' + columnIndex % 26));
            columnIndex = (columnIndex / 26) - 1;
        }
        return columnName.toString();
    }
}
