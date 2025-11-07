package com.example.cm_yt.video.service;

import com.example.cm_yt.script.entity.Script;
import com.example.cm_yt.script.repository.ScriptRepository;
import com.example.cm_yt.user.entity.User;
import com.example.cm_yt.user.repository.UserRepository;
import com.example.cm_yt.video.entity.Video;
import com.example.cm_yt.video.entity.VideoStatus;
import com.example.cm_yt.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoService {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final ScriptRepository scriptRepository;

    /**
     * 비디오 생성 (대기 상태로 시작)
     */
    @Transactional
    public Video createVideo(Long userId, Long scriptId, String title, String description, List<String> tags) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        Script script = scriptRepository.findById(scriptId)
                .orElseThrow(() -> new IllegalArgumentException("스크립트를 찾을 수 없습니다: " + scriptId));

        // 스크립트가 완료 상태인지 확인
        if (script.getImageGeneratedCount() == 0 || script.getTtsGeneratedCount() == 0) {
            log.warn("스크립트가 완료되지 않았습니다. ID: {}", scriptId);
        }

        Video video = Video.builder()
                .user(user)
                .script(script)
                .title(title)
                .description(description)
                .tags(tags)
                .build();

        return videoRepository.save(video);
    }

    /**
     * 비디오 조회
     */
    public Video findById(Long videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("비디오를 찾을 수 없습니다: " + videoId));
    }

    /**
     * 사용자의 모든 비디오 조회
     */
    public Page<Video> findByUserId(Long userId, Pageable pageable) {
        return videoRepository.findByUserId(userId, pageable);
    }

    /**
     * 사용자의 특정 상태 비디오 조회
     */
    public Page<Video> findByUserIdAndStatus(Long userId, VideoStatus status, Pageable pageable) {
        return videoRepository.findByUserIdAndStatus(userId, status, pageable);
    }

    /**
     * 비디오 제목/설명 수정
     */
    @Transactional
    public Video updateMetadata(Long videoId, String title, String description) {
        Video video = findById(videoId);
        if (title != null) {
            video.updateTitle(title);
        }
        if (description != null) {
            video.updateDescription(description);
        }
        return video;
    }

    /**
     * 비디오 상태 변경
     */
    @Transactional
    public Video updateStatus(Long videoId, VideoStatus status) {
        Video video = findById(videoId);
        video.updateStatus(status);
        return video;
    }

    /**
     * 영상 파일 생성 완료 처리
     * TODO: 실제 영상 생성 로직 구현 필요
     */
    @Transactional
    public Video completeVideoGeneration(Long videoId, String videoFileUrl, Integer videoDurationMs, String thumbnailUrl) {
        Video video = findById(videoId);
        video.updateVideoFile(videoFileUrl, videoDurationMs);
        if (thumbnailUrl != null) {
            video.updateThumbnail(thumbnailUrl);
        }
        return video;
    }

    /**
     * YouTube 업로드 완료 처리
     * TODO: 실제 YouTube API 연동 필요
     */
    @Transactional
    public Video completeYoutubeUpload(Long videoId, String youtubeVideoId, String youtubeUrl) {
        Video video = findById(videoId);
        video.updateYoutubeInfo(youtubeVideoId, youtubeUrl);
        return video;
    }

    /**
     * 비디오 처리 실패 처리
     */
    @Transactional
    public Video markAsFailed(Long videoId, String errorMessage) {
        Video video = findById(videoId);
        video.markAsFailed(errorMessage);
        return video;
    }

    /**
     * 태그 추가
     */
    @Transactional
    public Video addTag(Long videoId, String tag) {
        Video video = findById(videoId);
        video.addTag(tag);
        return video;
    }

    /**
     * 태그 삭제
     */
    @Transactional
    public Video removeTag(Long videoId, String tag) {
        Video video = findById(videoId);
        video.removeTag(tag);
        return video;
    }

    /**
     * 사용자의 최근 비디오 조회
     */
    public List<Video> getRecentVideos(Long userId) {
        return videoRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * 특정 상태의 비디오 조회 (배치 처리용)
     */
    public List<Video> findByStatus(VideoStatus status) {
        return videoRepository.findByStatus(status);
    }
}
