package com.ync.schedule.mapper;

import com.ync.schedule.domain.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    List<Department> findAll();

    Department findById(@Param("id") Long id);

    List<Department> findByParentId(@Param("parentId") Long parentId);

    List<Department> findByParentIdIsNull();

    List<Department> findByIsActiveTrue();

    int insert(Department department);

    int update(Department department);

    int deleteById(@Param("id") Long id);
}
