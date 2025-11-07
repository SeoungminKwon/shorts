package com.example.cm_yt.shorts.dto;

import com.example.cm_yt.shorts.entity.Shorts;
import com.example.cm_yt.shorts.entity.ShortsCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ShortsDetailResponse {

    private Long id;
    private String videoId;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String videoUrl;
    private String channelId;
    private String channelTitle;
    private LocalDateTime publishedAt;
    private Long viewCount;
    private Long likeCount;
    private Long channelSubscriberCount;
    private ShortsCategory category;
    private Double viewToSubscriberRatio;
    private List<String> tags;
    private String memo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ShortsDetailResponse from(Shorts shorts) {
        return ShortsDetailResponse.builder()
                .id(shorts.getId())
                .videoId(shorts.getVideoId())
                .title(shorts.getTitle())
                .description(shorts.getDescription())
                .thumbnailUrl(shorts.getThumbnailUrl())
                .videoUrl(shorts.getVideoUrl())
                .channelId(shorts.getChannelId())
                .channelTitle(shorts.getChannelTitle())
                .publishedAt(shorts.getPublishedAt())
                .viewCount(shorts.getViewCount())
                .likeCount(shorts.getLikeCount())
                .channelSubscriberCount(shorts.getChannelSubscriberCount())
                .category(shorts.getCategory())
                .viewToSubscriberRatio(shorts.getViewToSubscriberRatio())
                .tags(shorts.getTags())
                .memo(shorts.getMemo())
                .createdAt(shorts.getCreatedAt())
                .updatedAt(shorts.getUpdatedAt())
                .build();
    }
}
