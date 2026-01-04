package com.ync.intranet.service;

import com.ync.intranet.domain.ApprovalLineIntranet;
import com.ync.intranet.domain.DocumentIntranet;
import com.ync.intranet.domain.ExpenseReportIntranet;
import com.ync.intranet.domain.MemberIntranet;
import com.ync.intranet.mapper.ApprovalLineIntranetMapper;
import com.ync.intranet.mapper.DocumentIntranetMapper;
import com.ync.intranet.mapper.ExpenseReportIntranetMapper;
import com.ync.intranet.mapper.MemberIntranetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 경비보고서 서비스 (인트라넷)
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenseReportIntranetService {

    private final ExpenseReportIntranetMapper expenseReportMapper;
    private final DocumentIntranetMapper documentMapper;
    private final ApprovalLineIntranetMapper approvalLineMapper;
    private final MemberIntranetMapper memberMapper;

    /**
     * 경비보고서 조회 (ID)
     */
    public ExpenseReportIntranet getExpenseReportById(Long id) {
        return expenseReportMapper.findById(id);
    }

    /**
     * 문서 ID로 경비보고서 조회
     */
    public ExpenseReportIntranet getExpenseReportByDocumentId(Long documentId) {
        return expenseReportMapper.findByDocumentId(documentId);
    }

    /**
     * 전체 경비보고서 조회
     */
    public List<ExpenseReportIntranet> getAllExpenseReports() {
        return expenseReportMapper.findAll();
    }

    /**
     * 경비보고서 생성 (결재 요청 포함)
     */
    @Transactional
    public ExpenseReportIntranet createExpenseReport(ExpenseReportIntranet expenseReport, Long approverId) {
        // 1. 문서 생성
        DocumentIntranet document = new DocumentIntranet();
        document.setDocumentType(DocumentIntranet.DocumentType.EXPENSE);
        document.setAuthorId(expenseReport.getDocumentId()); // 임시, 실제로는 요청에서 받아야 함
        document.setTitle("경비보고서 - " + expenseReport.getReportMonth());
        document.setContent(expenseReport.getDescription() != null ? expenseReport.getDescription() : "");
        document.setStatus(DocumentIntranet.DocumentStatus.PENDING);
        document.setSubmittedAt(LocalDateTime.now());

        documentMapper.insert(document);

        // 2. 경비보고서에 문서 ID 설정
        expenseReport.setDocumentId(document.getId());

        // 3. 경비보고서 저장
        expenseReportMapper.insert(expenseReport);

        // 4. 결재선 생성
        if (approverId != null) {
            MemberIntranet approver = memberMapper.findById(approverId);
            if (approver != null) {
                ApprovalLineIntranet approvalLine = new ApprovalLineIntranet();
                approvalLine.setDocumentId(document.getId());
                approvalLine.setStepOrder(1);
                approvalLine.setApproverId(approverId);
                approvalLine.setApproverName(approver.getName());
                approvalLine.setApproverPosition(approver.getPosition() != null ? approver.getPosition() : "");
                approvalLine.setDecision(ApprovalLineIntranet.ApprovalDecision.PENDING);
                approvalLine.setSubmittedAt(LocalDateTime.now());

                approvalLineMapper.insert(approvalLine);
            }
        }

        return expenseReport;
    }

    /**
     * 경비보고서 수정
     */
    @Transactional
    public ExpenseReportIntranet updateExpenseReport(Long id, ExpenseReportIntranet expenseReport) {
        ExpenseReportIntranet existing = expenseReportMapper.findById(id);
        if (existing == null) {
            throw new RuntimeException("경비보고서를 찾을 수 없습니다: " + id);
        }

        // 결재 진행 중이거나 승인된 경우 수정 불가
        if (existing.getDocumentId() != null) {
            DocumentIntranet document = documentMapper.findById(existing.getDocumentId());
            if (document != null &&
                (document.getStatus() == DocumentIntranet.DocumentStatus.PENDING ||
                 document.getStatus() == DocumentIntranet.DocumentStatus.APPROVED)) {
                throw new RuntimeException("결재 진행 중이거나 승인된 경비보고서는 수정할 수 없습니다");
            }
        }

        existing.setReportMonth(expenseReport.getReportMonth());
        existing.setTotalAmount(expenseReport.getTotalAmount());
        existing.setDescription(expenseReport.getDescription());
        existing.setFilePath(expenseReport.getFilePath());

        expenseReportMapper.update(existing);
        return existing;
    }

    /**
     * 총 금액 업데이트 (항목 추가/삭제 시 사용)
     */
    @Transactional
    public void updateTotalAmount(Long id, java.math.BigDecimal totalAmount) {
        expenseReportMapper.updateTotalAmount(id, totalAmount);
    }

    /**
     * 경비보고서 삭제
     */
    @Transactional
    public void deleteExpenseReport(Long id) {
        ExpenseReportIntranet expenseReport = expenseReportMapper.findById(id);
        if (expenseReport == null) {
            throw new RuntimeException("경비보고서를 찾을 수 없습니다: " + id);
        }

        // 결재 진행 중이거나 승인된 경우 삭제 불가
        if (expenseReport.getDocumentId() != null) {
            DocumentIntranet document = documentMapper.findById(expenseReport.getDocumentId());
            if (document != null &&
                (document.getStatus() == DocumentIntranet.DocumentStatus.PENDING ||
                 document.getStatus() == DocumentIntranet.DocumentStatus.APPROVED)) {
                throw new RuntimeException("결재 진행 중이거나 승인된 경비보고서는 삭제할 수 없습니다");
            }
        }

        // 문서도 함께 삭제
        if (expenseReport.getDocumentId() != null) {
            documentMapper.deleteById(expenseReport.getDocumentId());
        }

        expenseReportMapper.deleteById(id);
    }
}
