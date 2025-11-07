package com.example.cm_yt.video.dto;

import com.example.cm_yt.video.entity.Video;
import com.example.cm_yt.video.entity.VideoStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class VideoResponse {

    private Long id;
    private Long userId;
    private Long scriptId;
    private String scriptTitle;
    private String title;
    private VideoStatus status;
    private String thumbnailUrl;
    private String videoFileUrl;
    private Integer videoDurationMs;
    private String youtubeVideoId;
    private String youtubeUrl;
    private LocalDateTime uploadedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static VideoResponse from(Video video) {
        return VideoResponse.builder()
                .id(video.getId())
                .userId(video.getUser().getId())
                .scriptId(video.getScript().getId())
                .scriptTitle(video.getScript().getTitle())
                .title(video.getTitle())
                .status(video.getStatus())
                .thumbnailUrl(video.getThumbnailUrl())
                .videoFileUrl(video.getVideoFileUrl())
                .videoDurationMs(video.getVideoDurationMs())
                .youtubeVideoId(video.getYoutubeVideoId())
                .youtubeUrl(video.getYoutubeUrl())
                .uploadedAt(video.getUploadedAt())
                .createdAt(video.getCreatedAt())
                .updatedAt(video.getUpdatedAt())
                .build();
    }
}
