package com.ync.schedule.service;

import com.ync.schedule.domain.Approval;
import com.ync.schedule.domain.Event;
import com.ync.schedule.domain.Member;
import com.ync.schedule.dto.ApprovalDto;
import com.ync.schedule.mapper.ApprovalMapper;
import com.ync.schedule.mapper.EventMapper;
import com.ync.schedule.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ApprovalService {

    private final ApprovalMapper approvalMapper;
    private final MemberMapper memberMapper;
    private final EventMapper eventMapper;
    private final EmailService emailService;

    public ApprovalService(ApprovalMapper approvalMapper, MemberMapper memberMapper,
                          EventMapper eventMapper, EmailService emailService) {
        this.approvalMapper = approvalMapper;
        this.memberMapper = memberMapper;
        this.eventMapper = eventMapper;
        this.emailService = emailService;
    }

    public List<ApprovalDto> getEventApprovals(Long eventId) {
        return approvalMapper.findByEventId(eventId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ApprovalDto> getApproverPendingApprovals(Long approverId) {
        return approvalMapper.findPendingByApproverId(approverId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ApprovalDto approve(Long approvalId, String comment) {
        Approval approval = approvalMapper.findById(approvalId);
        if (approval == null) {
            throw new RuntimeException("Approval not found: " + approvalId);
        }

        if (approval.getDecision() != null) {
            throw new RuntimeException("Approval already decided");
        }

        approval.setDecision(Approval.ApprovalDecision.APPROVED);
        approval.setComment(comment != null && comment.trim().isEmpty() ? null : comment);
        approval.setDecidedAt(LocalDateTime.now());
        approvalMapper.update(approval);

        // Update event status to APPROVED
        Event event = eventMapper.findById(approval.getEventId());
        if (event != null) {
            event.setStatus(Event.EventStatus.APPROVED);
            eventMapper.update(event);

            // Send approval email for LEAVE events
            if (event.getEventType() == Event.EventType.LEAVE && event.getMember() != null) {
                try {
                    Member approver = memberMapper.findById(approval.getApproverId());
                    String approverName = approver != null ? approver.getName() : "관리자";
                    String approverEmail = approver != null ? approver.getEmail() : null;
                    String approverSmtpPassword = approver != null ? approver.getSmtpPassword() : null;

                    if (event.getMember().getEmail() != null && !event.getMember().getEmail().isEmpty()) {
                        emailService.sendLeaveApprovalEmail(
                            approverEmail,
                            approverSmtpPassword,
                            event.getMember().getEmail(),
                            event.getMember().getName(),
                            event.getEventSubtype() != null ? event.getEventSubtype() : "휴가",
                            event.getStartDate(),
                            event.getEndDate(),
                            approverName
                        );
                    }
                } catch (Exception e) {
                    // 메일 발송 실패해도 승인은 진행
                    System.err.println("메일 발송 실패: " + e.getMessage());
                }
            }
        }

        return convertToDto(approval);
    }

    @Transactional
    public ApprovalDto reject(Long approvalId, String comment) {
        Approval approval = approvalMapper.findById(approvalId);
        if (approval == null) {
            throw new RuntimeException("Approval not found: " + approvalId);
        }

        if (approval.getDecision() != null) {
            throw new RuntimeException("Approval already decided");
        }

        approval.setDecision(Approval.ApprovalDecision.REJECTED);
        approval.setComment(comment != null && comment.trim().isEmpty() ? null : comment);
        approval.setDecidedAt(LocalDateTime.now());
        approvalMapper.update(approval);

        // Update event status to REJECTED
        Event event = eventMapper.findById(approval.getEventId());
        if (event != null) {
            event.setStatus(Event.EventStatus.REJECTED);
            eventMapper.update(event);

            // Send rejection email for LEAVE events
            if (event.getEventType() == Event.EventType.LEAVE && event.getMember() != null) {
                try {
                    Member approver = memberMapper.findById(approval.getApproverId());
                    String approverName = approver != null ? approver.getName() : "관리자";
                    String approverEmail = approver != null ? approver.getEmail() : null;
                    String approverSmtpPassword = approver != null ? approver.getSmtpPassword() : null;

                    if (event.getMember().getEmail() != null && !event.getMember().getEmail().isEmpty()) {
                        emailService.sendLeaveRejectionEmail(
                            approverEmail,
                            approverSmtpPassword,
                            event.getMember().getEmail(),
                            event.getMember().getName(),
                            event.getEventSubtype() != null ? event.getEventSubtype() : "휴가",
                            event.getStartDate(),
                            event.getEndDate(),
                            approverName,
                            comment
                        );
                    }
                } catch (Exception e) {
                    // 메일 발송 실패해도 반려는 진행
                    System.err.println("메일 발송 실패: " + e.getMessage());
                }
            }
        }

        return convertToDto(approval);
    }

    private ApprovalDto convertToDto(Approval approval) {
        // MyBatis가 join을 통해 approver 정보를 이미 채워줌
        String approverName = null;
        if (approval.getApprover() != null) {
            approverName = approval.getApprover().getName();
        }

        return ApprovalDto.builder()
                .id(approval.getId())
                .eventId(approval.getEventId())
                .stepOrder(approval.getStepOrder())
                .approverId(approval.getApproverId())
                .approverName(approverName)
                .decision(approval.getDecision())
                .comment(approval.getComment())
                .submittedAt(approval.getSubmittedAt())
                .decidedAt(approval.getDecidedAt())
                .build();
    }
}
