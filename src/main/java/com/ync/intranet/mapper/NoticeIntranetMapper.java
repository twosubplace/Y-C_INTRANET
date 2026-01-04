package com.ync.intranet.mapper;

import com.ync.intranet.domain.NoticeIntranet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 공지사항 Mapper (인트라넷)
 */
@Mapper
public interface NoticeIntranetMapper {

    /**
     * ID로 공지사항 조회
     */
    NoticeIntranet findById(@Param("id") Long id);

    /**
     * 전체 공지사항 조회 (최신순)
     */
    List<NoticeIntranet> findAll();

    /**
     * 고정 공지사항 조회
     */
    List<NoticeIntranet> findPinned();

    /**
     * 작성자별 공지사항 조회
     */
    List<NoticeIntranet> findByAuthorId(@Param("authorId") Long authorId);

    /**
     * 공지사항 등록
     */
    void insert(NoticeIntranet notice);

    /**
     * 공지사항 수정
     */
    void update(NoticeIntranet notice);

    /**
     * 조회수 증가
     */
    void incrementViewCount(@Param("id") Long id);

    /**
     * 공지사항 삭제
     */
    void deleteById(@Param("id") Long id);
}
