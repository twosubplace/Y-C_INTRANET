package com.ync.schedule.service;

import com.ync.schedule.domain.Department;
import com.ync.schedule.mapper.DepartmentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    public List<Department> getAllDepartments() {
        return departmentMapper.findAll();
    }

    public Department getDepartmentById(Long id) {
        return departmentMapper.findById(id);
    }

    public List<Department> getDepartmentsByParentId(Long parentId) {
        if (parentId == null) {
            return departmentMapper.findByParentIdIsNull();
        }
        return departmentMapper.findByParentId(parentId);
    }

    public List<Department> getTopLevelDepartments() {
        return departmentMapper.findByParentIdIsNull();
    }

    public List<Department> getActiveDepartments() {
        return departmentMapper.findByIsActiveTrue();
    }

    @Transactional
    public Department createDepartment(Department department) {
        departmentMapper.insert(department);
        return department;
    }

    @Transactional
    public Department updateDepartment(Long id, Department department) {
        department.setId(id);
        departmentMapper.update(department);
        return departmentMapper.findById(id);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        departmentMapper.deleteById(id);
    }
}
