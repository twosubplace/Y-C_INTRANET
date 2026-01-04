package com.ync.intranet.controller;

import com.ync.intranet.domain.ApprovalLineIntranet;
import com.ync.intranet.service.ApprovalService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 결재 컨트롤러 (인트라넷)
 */
@RestController("approvalControllerIntranet")
@RequestMapping("/api/intranet/approvals")
@CrossOrigin(origins = "*")
public class ApprovalController {

    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    /**
     * 내 대기중인 결재 목록
     * GET /api/intranet/approvals/pending
     */
    @GetMapping("/pending")
    public ResponseEntity<Map<String, Object>> getPendingApprovals(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        List<ApprovalLineIntranet> approvals = approvalService.getPendingApprovals(userId);
        return ResponseEntity.ok(Map.of("success", true, "approvals", approvals));
    }

    /**
     * 내 모든 결재 목록
     * GET /api/intranet/approvals/my
     */
    @GetMapping("/my")
    public ResponseEntity<Map<String, Object>> getMyApprovals(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        List<ApprovalLineIntranet> approvals = approvalService.getApprovalsByApproverId(userId);
        return ResponseEntity.ok(Map.of("success", true, "approvals", approvals));
    }

    /**
     * 문서의 결재선 조회
     * GET /api/intranet/approvals/document/{documentId}
     */
    @GetMapping("/document/{documentId}")
    public ResponseEntity<List<ApprovalLineIntranet>> getDocumentApprovals(@PathVariable Long documentId) {
        return ResponseEntity.ok(approvalService.getDocumentApprovals(documentId));
    }

    /**
     * 결재 승인
     * POST /api/intranet/approvals/{id}/approve
     */
    @PostMapping("/{id}/approve")
    public ResponseEntity<Map<String, Object>> approve(@PathVariable Long id,
                                                        @RequestBody Map<String, String> request,
                                                        HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(401)
                        .body(Map.of("success", false, "message", "로그인이 필요합니다."));
            }

            String comment = request.get("comment");
            approvalService.approve(id, userId, comment);

            return ResponseEntity.ok(Map.of("success", true, "message", "승인되었습니다."));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * 결재 반려
     * POST /api/intranet/approvals/{id}/reject
     */
    @PostMapping("/{id}/reject")
    public ResponseEntity<Map<String, Object>> reject(@PathVariable Long id,
                                                       @RequestBody Map<String, String> request,
                                                       HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(401)
                        .body(Map.of("success", false, "message", "로그인이 필요합니다."));
            }

            String comment = request.get("comment");
            approvalService.reject(id, userId, comment);

            return ResponseEntity.ok(Map.of("success", true, "message", "반려되었습니다."));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * 결재 취소 (작성자)
     * POST /api/intranet/approvals/document/{documentId}/cancel
     */
    @PostMapping("/document/{documentId}/cancel")
    public ResponseEntity<Map<String, Object>> cancelApproval(@PathVariable Long documentId,
                                                               HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(401)
                        .body(Map.of("success", false, "message", "로그인이 필요합니다."));
            }

            approvalService.cancelApproval(documentId, userId);

            return ResponseEntity.ok(Map.of("success", true, "message", "결재가 취소되었습니다."));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
