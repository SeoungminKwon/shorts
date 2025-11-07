package com.example.cm_yt.shorts.repository;

import com.example.cm_yt.shorts.entity.Shorts;
import com.example.cm_yt.shorts.entity.ShortsCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShortsRepository extends JpaRepository<Shorts, Long> {

    /**
     * 비디오 ID로 조회
     */
    Optional<Shorts> findByVideoId(String videoId);

    /**
     * 비디오 ID 존재 여부 확인
     */
    boolean existsByVideoId(String videoId);

    /**
     * 카테고리별 조회 (페이징)
     */
    Page<Shorts> findByCategory(ShortsCategory category, Pageable pageable);

    /**
     * 카테고리별 조회 및 조회수 순 정렬
     */
    Page<Shorts> findByCategoryOrderByViewCountDesc(ShortsCategory category, Pageable pageable);

    /**
     * 조회수 대비 구독자수 비율이 높은 순으로 정렬 (구독자는 적지만 조회수가 높은 쇼츠)
     * 이 쿼리는 엔티티의 getViewToSubscriberRatio() 메서드 로직을 SQL로 구현
     */
    @Query("SELECT s FROM Shorts s WHERE s.category = :category " +
           "ORDER BY (CAST(s.viewCount AS double) / NULLIF(s.channelSubscriberCount, 0)) DESC")
    Page<Shorts> findByCategoryOrderByViewToSubscriberRatioDesc(
            @Param("category") ShortsCategory category,
            Pageable pageable);

    /**
     * 태그로 검색
     */
    @Query("SELECT DISTINCT s FROM Shorts s JOIN s.tags t WHERE t = :tag")
    Page<Shorts> findByTag(@Param("tag") String tag, Pageable pageable);

    /**
     * 채널 ID로 검색
     */
    Page<Shorts> findByChannelId(String channelId, Pageable pageable);

    /**
     * 제목 또는 설명에 키워드 포함 검색
     */
    @Query("SELECT s FROM Shorts s WHERE s.title LIKE %:keyword% OR s.description LIKE %:keyword%")
    Page<Shorts> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 최근 등록된 쇼츠 조회
     */
    List<Shorts> findTop10ByOrderByCreatedAtDesc();
}
