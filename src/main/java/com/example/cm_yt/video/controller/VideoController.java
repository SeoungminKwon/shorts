package com.example.cm_yt.video.controller;

import com.example.cm_yt.global.ApiResponse;
import com.example.cm_yt.video.dto.VideoCreateRequest;
import com.example.cm_yt.video.dto.VideoDetailResponse;
import com.example.cm_yt.video.dto.VideoResponse;
import com.example.cm_yt.video.entity.Video;
import com.example.cm_yt.video.entity.VideoStatus;
import com.example.cm_yt.video.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Video", description = "영상 생성 및 관리 API")
@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @Operation(summary = "영상 생성 요청", description = "스크립트로부터 영상 생성을 요청합니다")
    @PostMapping
    public ApiResponse<VideoDetailResponse> createVideo(
            @RequestParam Long userId,
            @Valid @RequestBody VideoCreateRequest request) {
        Video video = videoService.createVideo(
                userId,
                request.getScriptId(),
                request.getTitle(),
                request.getDescription(),
                request.getTags()
        );
        return ApiResponse.success(VideoDetailResponse.from(video));
    }

    @Operation(summary = "영상 상세 조회", description = "영상 ID로 상세 정보를 조회합니다")
    @GetMapping("/{videoId}")
    public ApiResponse<VideoDetailResponse> getVideo(@PathVariable Long videoId) {
        Video video = videoService.findById(videoId);
        return ApiResponse.success(VideoDetailResponse.from(video));
    }

    @Operation(summary = "사용자 영상 목록", description = "사용자의 모든 영상을 조회합니다")
    @GetMapping("/user/{userId}")
    public ApiResponse<Page<VideoResponse>> getUserVideos(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Video> videos = videoService.findByUserId(userId, pageable);
        Page<VideoResponse> responses = videos.map(VideoResponse::from);
        return ApiResponse.success(responses);
    }

    @Operation(summary = "상태별 영상 조회", description = "사용자의 특정 상태 영상을 조회합니다")
    @GetMapping("/user/{userId}/status/{status}")
    public ApiResponse<Page<VideoResponse>> getUserVideosByStatus(
            @PathVariable Long userId,
            @PathVariable VideoStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Video> videos = videoService.findByUserIdAndStatus(userId, status, pageable);
        Page<VideoResponse> responses = videos.map(VideoResponse::from);
        return ApiResponse.success(responses);
    }

    @Operation(summary = "영상 메타데이터 수정", description = "영상 제목과 설명을 수정합니다")
    @PutMapping("/{videoId}/metadata")
    public ApiResponse<VideoDetailResponse> updateMetadata(
            @PathVariable Long videoId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description) {
        Video video = videoService.updateMetadata(videoId, title, description);
        return ApiResponse.success(VideoDetailResponse.from(video));
    }

    @Operation(summary = "영상 생성 시작", description = "영상 생성 프로세스를 시작합니다 (TODO: 실제 영상 생성 연동)")
    @PostMapping("/{videoId}/generate")
    public ApiResponse<VideoDetailResponse> startGeneration(@PathVariable Long videoId) {
        // TODO: 실제 영상 생성 로직 호출
        Video video = videoService.updateStatus(videoId, VideoStatus.GENERATING);
        return ApiResponse.success(VideoDetailResponse.from(video));
    }

    @Operation(summary = "영상 생성 완료", description = "영상 생성 완료를 처리합니다 (내부용 API)")
    @PostMapping("/{videoId}/complete-generation")
    public ApiResponse<VideoDetailResponse> completeGeneration(
            @PathVariable Long videoId,
            @RequestParam String videoFileUrl,
            @RequestParam Integer videoDurationMs,
            @RequestParam(required = false) String thumbnailUrl) {
        Video video = videoService.completeVideoGeneration(videoId, videoFileUrl, videoDurationMs, thumbnailUrl);
        return ApiResponse.success(VideoDetailResponse.from(video));
    }

    @Operation(summary = "YouTube 업로드 시작", description = "YouTube 업로드 프로세스를 시작합니다 (TODO: YouTube API 연동)")
    @PostMapping("/{videoId}/upload")
    public ApiResponse<VideoDetailResponse> startUpload(@PathVariable Long videoId) {
        // TODO: 실제 YouTube 업로드 로직 호출
        Video video = videoService.updateStatus(videoId, VideoStatus.UPLOADING);
        return ApiResponse.success(VideoDetailResponse.from(video));
    }

    @Operation(summary = "YouTube 업로드 완료", description = "YouTube 업로드 완료를 처리합니다 (내부용 API)")
    @PostMapping("/{videoId}/complete-upload")
    public ApiResponse<VideoDetailResponse> completeUpload(
            @PathVariable Long videoId,
            @RequestParam String youtubeVideoId,
            @RequestParam String youtubeUrl) {
        Video video = videoService.completeYoutubeUpload(videoId, youtubeVideoId, youtubeUrl);
        return ApiResponse.success(VideoDetailResponse.from(video));
    }

    @Operation(summary = "영상 처리 실패", description = "영상 생성 또는 업로드 실패를 기록합니다")
    @PostMapping("/{videoId}/fail")
    public ApiResponse<VideoDetailResponse> markAsFailed(
            @PathVariable Long videoId,
            @RequestParam String errorMessage) {
        Video video = videoService.markAsFailed(videoId, errorMessage);
        return ApiResponse.success(VideoDetailResponse.from(video));
    }

    @Operation(summary = "태그 추가", description = "영상에 태그를 추가합니다")
    @PostMapping("/{videoId}/tags")
    public ApiResponse<VideoDetailResponse> addTag(
            @PathVariable Long videoId,
            @RequestParam String tag) {
        Video video = videoService.addTag(videoId, tag);
        return ApiResponse.success(VideoDetailResponse.from(video));
    }

    @Operation(summary = "태그 삭제", description = "영상에서 태그를 삭제합니다")
    @DeleteMapping("/{videoId}/tags")
    public ApiResponse<VideoDetailResponse> removeTag(
            @PathVariable Long videoId,
            @RequestParam String tag) {
        Video video = videoService.removeTag(videoId, tag);
        return ApiResponse.success(VideoDetailResponse.from(video));
    }

    @Operation(summary = "최근 영상 목록", description = "사용자의 최근 영상 10개를 조회합니다")
    @GetMapping("/user/{userId}/recent")
    public ApiResponse<List<VideoResponse>> getRecentVideos(@PathVariable Long userId) {
        List<Video> videos = videoService.getRecentVideos(userId);
        List<VideoResponse> responses = videos.stream()
                .map(VideoResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success(responses);
    }
}
