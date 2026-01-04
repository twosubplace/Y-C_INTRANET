package com.ync.intranet.mapper;

import com.ync.intranet.domain.CommonCodeIntranet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 공통 코드 Mapper (인트라넷)
 */
@Mapper
public interface CommonCodeIntranetMapper {

    /**
     * 코드 타입별 조회
     */
    List<CommonCodeIntranet> findByCodeType(@Param("codeType") String codeType);

    /**
     * 코드 타입 + 코드로 조회
     */
    CommonCodeIntranet findByCodeTypeAndCode(@Param("codeType") String codeType,
                                              @Param("code") String code);

    /**
     * 활성화된 코드만 조회
     */
    List<CommonCodeIntranet> findActiveByCodeType(@Param("codeType") String codeType);

    /**
     * 전체 공통 코드 조회
     */
    List<CommonCodeIntranet> findAll();

    /**
     * 공통 코드 등록
     */
    void insert(CommonCodeIntranet commonCode);

    /**
     * 공통 코드 수정
     */
    void update(CommonCodeIntranet commonCode);

    /**
     * 공통 코드 삭제
     */
    void delete(@Param("codeType") String codeType, @Param("code") String code);
}
