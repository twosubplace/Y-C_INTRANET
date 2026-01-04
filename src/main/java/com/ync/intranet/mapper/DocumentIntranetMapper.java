package com.ync.intranet.mapper;

import com.ync.intranet.domain.DocumentIntranet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 문서 Mapper (인트라넷)
 */
@Mapper
public interface DocumentIntranetMapper {

    /**
     * 문서 조회 (ID)
     */
    DocumentIntranet findById(@Param("id") Long id);

    /**
     * 작성자별 문서 조회
     */
    List<DocumentIntranet> findByAuthorId(@Param("authorId") Long authorId);

    /**
     * 문서 유형별 조회
     */
    List<DocumentIntranet> findByDocumentType(@Param("documentType") String documentType);

    /**
     * 상태별 문서 조회
     */
    List<DocumentIntranet> findByStatus(@Param("status") String status);

    /**
     * 작성자 + 상태 조합 조회
     */
    List<DocumentIntranet> findByAuthorIdAndStatus(@Param("authorId") Long authorId,
                                                    @Param("status") String status);

    /**
     * 문서 유형 + 상태 조합 조회
     */
    List<DocumentIntranet> findByDocumentTypeAndStatus(@Param("documentType") String documentType,
                                                        @Param("status") String status);

    /**
     * 전체 문서 조회 (최신순)
     */
    List<DocumentIntranet> findAllOrderByCreatedAtDesc();

    /**
     * 문서 등록
     */
    void insert(DocumentIntranet document);

    /**
     * 문서 수정
     */
    void update(DocumentIntranet document);

    /**
     * 문서 상태 변경
     */
    void updateStatus(@Param("id") Long id, @Param("status") String status);

    /**
     * 문서 결재 상신
     */
    void submit(@Param("id") Long id);

    /**
     * 문서 최종 승인
     */
    void approve(@Param("id") Long id);

    /**
     * 문서 반려
     */
    void reject(@Param("id") Long id);

    /**
     * 문서 삭제
     */
    void deleteById(@Param("id") Long id);
}
