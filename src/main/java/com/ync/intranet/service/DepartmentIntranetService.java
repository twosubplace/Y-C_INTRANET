package com.ync.intranet.service;

import com.ync.intranet.domain.DepartmentIntranet;
import com.ync.intranet.mapper.DepartmentIntranetMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 부서 관리 서비스 (인트라넷)
 */
@Service("departmentIntranetService")
@Transactional(readOnly = true)
public class DepartmentIntranetService {

    private final DepartmentIntranetMapper departmentMapper;

    public DepartmentIntranetService(DepartmentIntranetMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    /**
     * 전체 부서 조회
     */
    public List<DepartmentIntranet> findAll() {
        return departmentMapper.findAll();
    }

    /**
     * 활성화된 부서만 조회
     */
    public List<DepartmentIntranet> findActiveDepartments() {
        return departmentMapper.findByIsActiveTrue();
    }

    /**
     * ID로 부서 조회
     */
    public DepartmentIntranet findById(Long id) {
        return departmentMapper.findById(id);
    }

    /**
     * 최상위 부서 조회 (parent_id가 null인 부서)
     */
    public List<DepartmentIntranet> findRootDepartments() {
        return departmentMapper.findRootDepartments();
    }

    /**
     * 상위 부서별 하위 부서 조회
     */
    public List<DepartmentIntranet> findByParentId(Long parentId) {
        return departmentMapper.findByParentId(parentId);
    }

    /**
     * 부서 등록
     */
    @Transactional
    public DepartmentIntranet createDepartment(DepartmentIntranet department) {
        // 기본값 설정
        if (department.getDisplayOrder() == null) {
            department.setDisplayOrder(0);
        }
        if (department.getIsActive() == null) {
            department.setIsActive(true);
        }

        departmentMapper.insert(department);
        return department;
    }

    /**
     * 부서 수정
     */
    @Transactional
    public DepartmentIntranet updateDepartment(DepartmentIntranet department) {
        DepartmentIntranet existing = departmentMapper.findById(department.getId());
        if (existing == null) {
            throw new RuntimeException("존재하지 않는 부서입니다.");
        }

        departmentMapper.update(department);
        return department;
    }

    /**
     * 부서 비활성화
     */
    @Transactional
    public void deactivateDepartment(Long id) {
        departmentMapper.deactivate(id);
    }

    /**
     * 부서 삭제 (물리 삭제 - 주의!)
     */
    @Transactional
    public void deleteDepartment(Long id) {
        departmentMapper.deleteById(id);
    }
}
