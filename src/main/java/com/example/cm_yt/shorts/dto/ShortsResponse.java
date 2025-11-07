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
public class ShortsResponse {

    private Long id;
    private String videoId;
    private String title;
    private String thumbnailUrl;
    private String channelTitle;
    private LocalDateTime publishedAt;
    private Long viewCount;
    private Long likeCount;
    private Long channelSubscriberCount;
    private ShortsCategory category;
    private Double viewToSubscriberRatio; // 조회수/구독자 비율
    private List<String> tags;
    private LocalDateTime createdAt;

    public static ShortsResponse from(Shorts shorts) {
        return ShortsResponse.builder()
                .id(shorts.getId())
                .videoId(shorts.getVideoId())
                .title(shorts.getTitle())
                .thumbnailUrl(shorts.getThumbnailUrl())
                .channelTitle(shorts.getChannelTitle())
                .publishedAt(shorts.getPublishedAt())
                .viewCount(shorts.getViewCount())
                .likeCount(shorts.getLikeCount())
                .channelSubscriberCount(shorts.getChannelSubscriberCount())
                .category(shorts.getCategory())
                .viewToSubscriberRatio(shorts.getViewToSubscriberRatio())
                .tags(shorts.getTags())
                .createdAt(shorts.getCreatedAt())
                .build();
    }
}
