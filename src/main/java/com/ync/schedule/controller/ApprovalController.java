package com.ync.schedule.controller;

import com.ync.schedule.dto.ApprovalDto;
import com.ync.schedule.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/approvals")
@CrossOrigin(origins = "*")
public class ApprovalController {

    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<ApprovalDto>> getEventApprovals(@PathVariable Long eventId) {
        List<ApprovalDto> approvals = approvalService.getEventApprovals(eventId);
        return ResponseEntity.ok(approvals);
    }

    @GetMapping("/approver/{approverId}/pending")
    public ResponseEntity<List<ApprovalDto>> getPendingApprovals(@PathVariable Long approverId) {
        List<ApprovalDto> approvals = approvalService.getApproverPendingApprovals(approverId);
        return ResponseEntity.ok(approvals);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ApprovalDto> approveApproval(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String comment = payload.get("comment");
        ApprovalDto approved = approvalService.approve(id, comment);
        return ResponseEntity.ok(approved);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ApprovalDto> rejectApproval(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String comment = payload.get("comment");
        ApprovalDto rejected = approvalService.reject(id, comment);
        return ResponseEntity.ok(rejected);
    }
}
