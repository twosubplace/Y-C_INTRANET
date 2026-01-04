package com.ync.intranet.service;

import com.ync.intranet.domain.DepartmentIntranet;
import com.ync.intranet.domain.MemberIntranet;
import com.ync.intranet.mapper.DepartmentIntranetMapper;
import com.ync.intranet.mapper.MemberIntranetMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 인증 서비스 (인트라넷)
 * Session 기반 로그인/로그아웃 처리
 */
@Service("authServiceIntranet")
@Transactional(readOnly = true)
public class AuthService {

    private final MemberIntranetMapper memberMapper;
    private final DepartmentIntranetMapper departmentMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthService(MemberIntranetMapper memberMapper, DepartmentIntranetMapper departmentMapper) {
        this.memberMapper = memberMapper;
        this.departmentMapper = departmentMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * 로그인
     * @param email 이메일
     * @param password 비밀번호 (평문)
     * @return 로그인 성공한 사용자 정보 (비밀번호 제외)
     */
    public MemberIntranet login(String email, String password) {
        // 1. 이메일로 사용자 조회
        MemberIntranet member = memberMapper.findByEmail(email);

        if (member == null) {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }

        // 2. 활성화 여부 확인
        if (!member.getIsActive()) {
            throw new RuntimeException("비활성화된 계정입니다.");
        }

        // 3. 비밀번호 확인
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 4. 비밀번호 제거 후 반환 (보안)
        member.setPassword(null);
        member.setSmtpPassword(null);

        return member;
    }

    /**
     * 비밀번호 변경
     * @param memberId 사용자 ID
     * @param oldPassword 기존 비밀번호
     * @param newPassword 새 비밀번호
     */
    @Transactional
    public void changePassword(Long memberId, String oldPassword, String newPassword) {
        // 1. 사용자 조회
        MemberIntranet member = memberMapper.findById(memberId);

        if (member == null) {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }

        // 2. 기존 비밀번호 확인
        if (!passwordEncoder.matches(oldPassword, member.getPassword())) {
            throw new RuntimeException("기존 비밀번호가 일치하지 않습니다.");
        }

        // 3. 새 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(newPassword);

        // 4. 비밀번호 업데이트
        memberMapper.updatePassword(memberId, encodedPassword);
    }

    /**
     * 비밀번호 암호화 (관리자용)
     * @param plainPassword 평문 비밀번호
     * @return 암호화된 비밀번호
     */
    public String encodePassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    /**
     * 비밀번호 확인 (네이버웍스 로그인용)
     * @param plainPassword 평문 비밀번호
     * @param encodedPassword 암호화된 비밀번호
     * @return 일치 여부
     */
    public boolean checkPassword(String plainPassword, String encodedPassword) {
        return passwordEncoder.matches(plainPassword, encodedPassword);
    }

    /**
     * 결재자 조회 (조직도 기반)
     * @param userId 사용자 ID
     * @return 결재자 정보 (id, name, position, departmentName)
     */
    public Map<String, Object> getApproverForUser(Long userId) {
        // 1. 사용자 정보 조회
        MemberIntranet member = memberMapper.findById(userId);
        if (member == null || member.getDepartmentId() == null) {
            return null;
        }

        // 2. 사용자의 부서 정보 조회
        DepartmentIntranet userDepartment = departmentMapper.findById(member.getDepartmentId());
        if (userDepartment == null) {
            return null;
        }

        String position = member.getPosition() != null ? member.getPosition() : "";

        // 3. 직급에 따른 결재자 결정
        MemberIntranet approver = null;

        // 3-1. 매니저 또는 사원인 경우: 해당 부서의 Unit장 (부서장)
        if (position.equals("매니저") || position.equals("사원") || position.equals("")) {
            if (userDepartment.getManagerId() != null) {
                approver = memberMapper.findById(userDepartment.getManagerId());
            }
        }
        // 3-2. Unit장인 경우: 상위 부서(본부)의 본부장
        else if (position.equals("Unit장")) {
            if (userDepartment.getParentId() != null) {
                DepartmentIntranet parentDepartment = departmentMapper.findById(userDepartment.getParentId());
                if (parentDepartment != null && parentDepartment.getManagerId() != null) {
                    approver = memberMapper.findById(parentDepartment.getManagerId());
                }
            }
        }
        // 3-3. 본부장인 경우: CEO 또는 대표
        else if (position.equals("본부장")) {
            // 본부의 상위 부서가 있다면 그 부서의 매니저 (CEO)
            if (userDepartment.getParentId() != null) {
                DepartmentIntranet topDepartment = departmentMapper.findById(userDepartment.getParentId());
                if (topDepartment != null && topDepartment.getManagerId() != null) {
                    approver = memberMapper.findById(topDepartment.getManagerId());
                }
            }
        }

        // 4. 결재자 정보 반환
        if (approver == null) {
            return null;
        }

        Map<String, Object> approverInfo = new HashMap<>();
        approverInfo.put("id", approver.getId());
        approverInfo.put("name", approver.getName());
        approverInfo.put("position", approver.getPosition() != null ? approver.getPosition() : "");
        approverInfo.put("departmentName", approver.getDepartmentName() != null ? approver.getDepartmentName() : "");
        approverInfo.put("email", approver.getEmail());

        return approverInfo;
    }

    /**
     * 결재 가능한 사용자 목록 조회 (APPROVER, ADMIN 권한)
     * @return 결재 가능한 사용자 목록
     */
    public List<Map<String, Object>> getApproverList() {
        // APPROVER 또는 ADMIN 권한을 가진 활성 사용자 조회
        List<MemberIntranet> approvers = memberMapper.findByIsActiveTrue();

        return approvers.stream()
                .filter(m -> "APPROVER".equals(m.getRole()) || "ADMIN".equals(m.getRole()))
                .map(m -> {
                    Map<String, Object> info = new HashMap<>();
                    info.put("id", m.getId());
                    info.put("name", m.getName());
                    info.put("position", m.getPosition() != null ? m.getPosition() : "");
                    info.put("departmentName", m.getDepartmentName() != null ? m.getDepartmentName() : "");
                    info.put("email", m.getEmail());
                    return info;
                })
                .collect(Collectors.toList());
    }
}
