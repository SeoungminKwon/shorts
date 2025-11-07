package com.example.cm_yt.video.repository;

import com.example.cm_yt.video.entity.Video;
import com.example.cm_yt.video.entity.VideoStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    /**
     * 사용자의 모든 영상 조회
     */
    Page<Video> findByUserId(Long userId, Pageable pageable);

    /**
     * 사용자의 특정 상태 영상 조회
     */
    Page<Video> findByUserIdAndStatus(Long userId, VideoStatus status, Pageable pageable);

    /**
     * 스크립트로부터 생성된 영상 조회
     */
    List<Video> findByScriptId(Long scriptId);

    /**
     * YouTube 비디오 ID로 조회
     */
    Optional<Video> findByYoutubeVideoId(String youtubeVideoId);

    /**
     * 사용자의 최근 영상 조회
     */
    List<Video> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * 특정 상태의 모든 영상 조회 (배치 처리용)
     */
    List<Video> findByStatus(VideoStatus status);
}
