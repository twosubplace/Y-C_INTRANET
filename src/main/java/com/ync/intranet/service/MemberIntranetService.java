package com.ync.intranet.service;

import com.ync.intranet.domain.MemberIntranet;
import com.ync.intranet.mapper.MemberIntranetMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 사원 관리 서비스 (인트라넷)
 */
@Service("memberIntranetService")
@Transactional(readOnly = true)
public class MemberIntranetService {

    private final MemberIntranetMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    public MemberIntranetService(MemberIntranetMapper memberMapper) {
        this.memberMapper = memberMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * 전체 사원 조회
     */
    public List<MemberIntranet> findAll() {
        return memberMapper.findAll();
    }

    /**
     * ID로 사원 조회
     */
    public MemberIntranet findById(Long id) {
        MemberIntranet member = memberMapper.findById(id);
        if (member != null) {
            member.setPassword(null);  // 비밀번호 제거
            member.setSmtpPassword(null);
        }
        return member;
    }

    /**
     * 활성화된 사원만 조회
     */
    public List<MemberIntranet> findActiveMembers() {
        return memberMapper.findByIsActiveTrue();
    }

    /**
     * 부서별 사원 조회
     */
    public List<MemberIntranet> findByDepartmentId(Long departmentId) {
        return memberMapper.findByDepartmentId(departmentId);
    }

    /**
     * 권한별 사원 조회
     */
    public List<MemberIntranet> findByRole(String role) {
        return memberMapper.findByRole(role);
    }

    /**
     * 사원 등록
     */
    @Transactional
    public MemberIntranet createMember(MemberIntranet member) {
        // 이메일 중복 확인
        MemberIntranet existing = memberMapper.findByEmail(member.getEmail());
        if (existing != null) {
            throw new RuntimeException("이미 존재하는 이메일입니다: " + member.getEmail());
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encodedPassword);

        // 기본값 설정
        if (member.getRole() == null) {
            member.setRole("USER");
        }
        if (member.getIsActive() == null) {
            member.setIsActive(true);
        }

        memberMapper.insert(member);

        // 비밀번호 제거 후 반환
        member.setPassword(null);
        return member;
    }

    /**
     * 사원 수정
     */
    @Transactional
    public MemberIntranet updateMember(MemberIntranet member) {
        MemberIntranet existing = memberMapper.findById(member.getId());
        if (existing == null) {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }

        // 비밀번호는 별도 메서드로만 변경 가능
        member.setPassword(existing.getPassword());

        memberMapper.update(member);

        member.setPassword(null);
        return member;
    }

    /**
     * 사원 비활성화 (퇴사 처리)
     */
    @Transactional
    public void deactivateMember(Long id) {
        memberMapper.deactivate(id);
    }

    /**
     * 사원 삭제 (물리 삭제 - 주의!)
     */
    @Transactional
    public void deleteMember(Long id) {
        memberMapper.deleteById(id);
    }

    /**
     * 이메일로 사원 조회 (네이버웍스 연동용)
     */
    public MemberIntranet findByEmail(String email) {
        MemberIntranet member = memberMapper.findByEmail(email);
        if (member != null) {
            member.setPassword(null);  // 비밀번호 제거
            member.setSmtpPassword(null);
        }
        return member;
    }
}
