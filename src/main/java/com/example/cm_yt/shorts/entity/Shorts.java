package com.example.cm_yt.shorts.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shorts", indexes = {
    @Index(name = "idx_video_id", columnList = "videoId"),
    @Index(name = "idx_category", columnList = "category"),
    @Index(name = "idx_view_count", columnList = "viewCount"),
    @Index(name = "idx_published_at", columnList = "publishedAt")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shorts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String videoId; // 유튜브 비디오 ID

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 5000)
    private String description;

    @Column(nullable = false, length = 500)
    private String thumbnailUrl;

    @Column(nullable = false, length = 500)
    private String videoUrl;

    @Column(nullable = false, length = 100)
    private String channelId;

    @Column(nullable = false, length = 100)
    private String channelTitle;

    @Column(nullable = false)
    private LocalDateTime publishedAt; // 유튜브 업로드 날짜

    @Column(nullable = false)
    private Long viewCount = 0L;

    @Column(nullable = false)
    private Long likeCount = 0L;

    @Column(nullable = false)
    private Long channelSubscriberCount = 0L; // 채널 구독자 수 (알고리즘에 사용)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ShortsCategory category;

    @ElementCollection
    @CollectionTable(name = "shorts_tags", joinColumns = @JoinColumn(name = "shorts_id"))
    @Column(name = "tag", length = 50)
    private List<String> tags = new ArrayList<>();

    @Column(length = 1000)
    private String memo; // 사용자 메모

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Shorts(String videoId, String title, String description, String thumbnailUrl,
                  String videoUrl, String channelId, String channelTitle, LocalDateTime publishedAt,
                  Long viewCount, Long likeCount, Long channelSubscriberCount,
                  ShortsCategory category, List<String> tags) {
        this.videoId = videoId;
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.channelId = channelId;
        this.channelTitle = channelTitle;
        this.publishedAt = publishedAt;
        this.viewCount = viewCount != null ? viewCount : 0L;
        this.likeCount = likeCount != null ? likeCount : 0L;
        this.channelSubscriberCount = channelSubscriberCount != null ? channelSubscriberCount : 0L;
        this.category = category;
        this.tags = tags != null ? tags : new ArrayList<>();
    }

    public void updateStatistics(Long viewCount, Long likeCount) {
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }

    public void updateMemo(String memo) {
        this.memo = memo;
    }

    public void addTag(String tag) {
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    public void removeTag(String tag) {
        this.tags.remove(tag);
    }

    /**
     * 조회수 대비 구독자 수 비율 계산
     * 구독자가 적을수록 값이 커짐 (높은 조회수 / 낮은 구독자수 = 높은 값)
     */
    public double getViewToSubscriberRatio() {
        if (channelSubscriberCount == 0) {
            return viewCount.doubleValue(); // 구독자가 0이면 조회수 그대로 반환
        }
        return viewCount.doubleValue() / channelSubscriberCount.doubleValue();
    }
}
