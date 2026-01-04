package com.ync.schedule.mapper;

import com.ync.schedule.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {

    List<Member> findAll();

    Member findById(@Param("id") Long id);

    List<Member> findByIsActiveTrue();

    List<Member> findByDepartment(@Param("department") String department);

    List<Member> findAllActiveOrderByDepartmentAndName();

    int insert(Member member);

    int update(Member member);

    int deleteById(@Param("id") Long id);
}
