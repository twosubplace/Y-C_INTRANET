package com.ync.intranet.service;

import com.ync.intranet.domain.ApprovalLineIntranet;
import com.ync.intranet.domain.DocumentIntranet;
import com.ync.intranet.domain.MemberIntranet;
import com.ync.intranet.domain.ScheduleIntranet;
import com.ync.intranet.mapper.ApprovalLineIntranetMapper;
import com.ync.intranet.mapper.DocumentIntranetMapper;
import com.ync.intranet.mapper.MemberIntranetMapper;
import com.ync.intranet.mapper.ScheduleIntranetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 일정/휴가 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleIntranetService {

    private final ScheduleIntranetMapper scheduleMapper;
    private final DocumentIntranetMapper documentMapper;
    private final ApprovalLineIntranetMapper approvalLineMapper;
    private final MemberIntranetMapper memberMapper;

    /**
     * 일정 생성
     */
    @Transactional
    public void createSchedule(ScheduleIntranet schedule) {
        // 기본값 설정
        if (schedule.getStatus() == null) {
            schedule.setStatus("DRAFT");
        }
        if (schedule.getDaysUsed() == null) {
            schedule.setDaysUsed(0.0);
        }

        // 연차/반차인 경우 문서 생성 및 결재 요청
        if (("VACATION".equals(schedule.getScheduleType()) || "HALF_DAY".equals(schedule.getScheduleType()))
                && schedule.getApproverId() != null) {

            // 1. 문서 생성
            DocumentIntranet document = new DocumentIntranet();
            document.setDocumentType(DocumentIntranet.DocumentType.LEAVE);
            document.setAuthorId(schedule.getMemberId());
            document.setTitle(schedule.getTitle());
            document.setContent(schedule.getDescription() != null ? schedule.getDescription() : "");
            document.setStatus(DocumentIntranet.DocumentStatus.PENDING);
            document.setSubmittedAt(LocalDateTime.now());

            documentMapper.insert(document);

            // 2. 일정에 문서 ID 연결
            schedule.setDocumentId(document.getId());
            schedule.setStatus("SUBMITTED");  // 결재 대기 상태

            // 3. 결재선 생성
            MemberIntranet approver = memberMapper.findById(schedule.getApproverId());
            if (approver != null) {
                ApprovalLineIntranet approvalLine = new ApprovalLineIntranet();
                approvalLine.setDocumentId(document.getId());
                approvalLine.setStepOrder(1);
                approvalLine.setApproverId(schedule.getApproverId());
                approvalLine.setApproverName(approver.getName());
                approvalLine.setApproverPosition(approver.getPosition() != null ? approver.getPosition() : "");
                approvalLine.setDecision(ApprovalLineIntranet.ApprovalDecision.PENDING);
                approvalLine.setSubmittedAt(LocalDateTime.now());

                approvalLineMapper.insert(approvalLine);
            }
        }

        // 일정 저장
        scheduleMapper.insert(schedule);
    }

    /**
     * 일정 수정
     */
    @Transactional
    public void updateSchedule(ScheduleIntranet schedule) {
        scheduleMapper.update(schedule);
    }

    /**
     * 일정 삭제
     */
    @Transactional
    public void deleteSchedule(Long id) {
        scheduleMapper.delete(id);
    }

    /**
     * ID로 일정 조회
     */
    public ScheduleIntranet getScheduleById(Long id) {
        ScheduleIntranet schedule = scheduleMapper.findById(id);

        // 연차/반차이고 문서가 연결된 경우, 문서 상태와 일정 상태 동기화
        if (schedule != null && schedule.getDocumentId() != null) {
            DocumentIntranet document = documentMapper.findById(schedule.getDocumentId());
            if (document != null) {
                // 문서 상태를 일정 상태로 매핑
                String documentStatus = document.getStatus().name();

                // PENDING -> SUBMITTED (프론트엔드에서는 SUBMITTED로 사용)
                if ("PENDING".equals(documentStatus)) {
                    schedule.setStatus("SUBMITTED");
                } else {
                    schedule.setStatus(documentStatus);
                }
            }
        }

        return schedule;
    }

    /**
     * 전체 일정 조회
     */
    public List<ScheduleIntranet> getAllSchedules() {
        return scheduleMapper.findAll();
    }

    /**
     * 특정 사용자의 일정 조회
     */
    public List<ScheduleIntranet> getSchedulesByMemberId(Long memberId) {
        return scheduleMapper.findByMemberId(memberId);
    }

    /**
     * 기간별 일정 조회
     */
    public List<ScheduleIntranet> getSchedulesByDateRange(Date startDate, Date endDate) {
        return scheduleMapper.findByDateRange(startDate, endDate);
    }

    /**
     * 부서별 일정 조회
     */
    public List<ScheduleIntranet> getSchedulesByDepartmentAndDateRange(
            Long departmentId, Date startDate, Date endDate) {
        return scheduleMapper.findByDepartmentAndDateRange(departmentId, startDate, endDate);
    }

    /**
     * 일정 취소 신청 (승인된 연차/반차)
     * 기존 문서를 복사하여 취소 문서를 생성하고 결재 요청
     */
    @Transactional
    public void requestCancellation(Long scheduleId) {
        ScheduleIntranet schedule = scheduleMapper.findById(scheduleId);
        if (schedule == null) {
            throw new RuntimeException("일정을 찾을 수 없습니다.");
        }

        // 기존 문서 확인
        if (schedule.getDocumentId() == null) {
            throw new RuntimeException("연결된 문서가 없습니다.");
        }

        DocumentIntranet originalDocument = documentMapper.findById(schedule.getDocumentId());
        if (originalDocument == null) {
            throw new RuntimeException("원본 문서를 찾을 수 없습니다.");
        }

        // 기존 결재선 조회 (동일한 결재자에게 취소 결재 요청)
        List<ApprovalLineIntranet> originalApprovals = approvalLineMapper.findByDocumentId(originalDocument.getId());
        if (originalApprovals.isEmpty()) {
            throw new RuntimeException("결재선 정보가 없습니다.");
        }

        // 1. 취소 문서 생성
        DocumentIntranet cancelDocument = new DocumentIntranet();
        cancelDocument.setDocumentType(DocumentIntranet.DocumentType.LEAVE);
        cancelDocument.setAuthorId(schedule.getMemberId());
        cancelDocument.setTitle("[취소] " + schedule.getTitle());
        cancelDocument.setContent("원본 일정 취소 요청\n\n" + (schedule.getDescription() != null ? schedule.getDescription() : ""));
        cancelDocument.setStatus(DocumentIntranet.DocumentStatus.PENDING);
        cancelDocument.setSubmittedAt(LocalDateTime.now());

        documentMapper.insert(cancelDocument);

        // 2. 취소 결재선 생성 (동일한 결재자)
        for (ApprovalLineIntranet originalApproval : originalApprovals) {
            ApprovalLineIntranet cancelApprovalLine = new ApprovalLineIntranet();
            cancelApprovalLine.setDocumentId(cancelDocument.getId());
            cancelApprovalLine.setStepOrder(originalApproval.getStepOrder());
            cancelApprovalLine.setApproverId(originalApproval.getApproverId());
            cancelApprovalLine.setApproverName(originalApproval.getApproverName());
            cancelApprovalLine.setApproverPosition(originalApproval.getApproverPosition());
            cancelApprovalLine.setDecision(ApprovalLineIntranet.ApprovalDecision.PENDING);
            cancelApprovalLine.setSubmittedAt(LocalDateTime.now());

            approvalLineMapper.insert(cancelApprovalLine);
        }

        // 3. 일정 상태를 PENDING으로 변경하고 취소 문서 ID 저장
        // 참고: 취소 승인 시 일정을 CANCELLED로 변경하고 삭제하는 로직은 ApprovalIntranetService에서 처리
        schedule.setStatus("PENDING");
        // 취소 문서 ID를 별도로 저장할 필요가 있다면 metadata나 별도 필드 활용
        // 여기서는 간단히 상태만 변경
        scheduleMapper.update(schedule);
    }
}
