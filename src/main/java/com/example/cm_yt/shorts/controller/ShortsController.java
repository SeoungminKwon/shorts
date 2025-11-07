package com.example.cm_yt.shorts.controller;

import com.example.cm_yt.global.ApiResponse;
import com.example.cm_yt.shorts.dto.*;
import com.example.cm_yt.shorts.entity.Shorts;
import com.example.cm_yt.shorts.service.ShortsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Shorts", description = "쇼츠 관리 API")
@RestController
@RequestMapping("/api/shorts")
@RequiredArgsConstructor
public class ShortsController {

    private final ShortsService shortsService;

    @Operation(summary = "쇼츠 등록", description = "YouTube 쇼츠 정보를 등록합니다")
    @PostMapping
    public ApiResponse<ShortsDetailResponse> registerShorts(@Valid @RequestBody ShortsRegisterRequest request) {
        Shorts shorts = shortsService.registerShorts(
                request.getVideoId(),
                request.getTitle(),
                request.getDescription(),
                request.getThumbnailUrl(),
                request.getVideoUrl(),
                request.getChannelId(),
                request.getChannelTitle(),
                request.getPublishedAt(),
                request.getViewCount(),
                request.getLikeCount(),
                request.getChannelSubscriberCount(),
                request.getCategory(),
                request.getTags()
        );
        return ApiResponse.success(ShortsDetailResponse.from(shorts));
    }

    @Operation(summary = "쇼츠 검색", description = "카테고리, 키워드, 태그로 쇼츠를 검색합니다")
    @PostMapping("/search")
    public ApiResponse<Page<ShortsResponse>> searchShorts(@RequestBody ShortsSearchRequest request) {
        Page<Shorts> shortsPage = shortsService.searchShorts(request);
        Page<ShortsResponse> responsePage = shortsPage.map(ShortsResponse::from);
        return ApiResponse.success(responsePage);
    }

    @Operation(summary = "쇼츠 상세 조회", description = "쇼츠 ID로 상세 정보를 조회합니다")
    @GetMapping("/{id}")
    public ApiResponse<ShortsDetailResponse> getShorts(@PathVariable Long id) {
        Shorts shorts = shortsService.findById(id);
        return ApiResponse.success(ShortsDetailResponse.from(shorts));
    }

    @Operation(summary = "비디오 ID로 쇼츠 조회", description = "YouTube 비디오 ID로 쇼츠를 조회합니다")
    @GetMapping("/video/{videoId}")
    public ApiResponse<ShortsDetailResponse> getShortsByVideoId(@PathVariable String videoId) {
        Shorts shorts = shortsService.findByVideoId(videoId);
        return ApiResponse.success(ShortsDetailResponse.from(shorts));
    }

    @Operation(summary = "최근 쇼츠 목록", description = "최근 등록된 쇼츠 10개를 조회합니다")
    @GetMapping("/recent")
    public ApiResponse<List<ShortsResponse>> getRecentShorts() {
        List<Shorts> recentShorts = shortsService.getRecentShorts();
        List<ShortsResponse> responses = recentShorts.stream()
                .map(ShortsResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success(responses);
    }

    @Operation(summary = "쇼츠 통계 업데이트", description = "조회수와 좋아요 수를 업데이트합니다")
    @PutMapping("/{id}/statistics")
    public ApiResponse<ShortsDetailResponse> updateStatistics(
            @PathVariable Long id,
            @RequestParam Long viewCount,
            @RequestParam Long likeCount) {
        Shorts shorts = shortsService.updateStatistics(id, viewCount, likeCount);
        return ApiResponse.success(ShortsDetailResponse.from(shorts));
    }

    @Operation(summary = "쇼츠 메모 업데이트", description = "쇼츠에 메모를 추가/수정합니다")
    @PutMapping("/{id}/memo")
    public ApiResponse<ShortsDetailResponse> updateMemo(
            @PathVariable Long id,
            @RequestParam String memo) {
        Shorts shorts = shortsService.updateMemo(id, memo);
        return ApiResponse.success(ShortsDetailResponse.from(shorts));
    }

    @Operation(summary = "태그 추가", description = "쇼츠에 태그를 추가합니다")
    @PostMapping("/{id}/tags")
    public ApiResponse<ShortsDetailResponse> addTag(
            @PathVariable Long id,
            @RequestParam String tag) {
        Shorts shorts = shortsService.addTag(id, tag);
        return ApiResponse.success(ShortsDetailResponse.from(shorts));
    }

    @Operation(summary = "태그 삭제", description = "쇼츠에서 태그를 삭제합니다")
    @DeleteMapping("/{id}/tags")
    public ApiResponse<ShortsDetailResponse> removeTag(
            @PathVariable Long id,
            @RequestParam String tag) {
        Shorts shorts = shortsService.removeTag(id, tag);
        return ApiResponse.success(ShortsDetailResponse.from(shorts));
    }
}
