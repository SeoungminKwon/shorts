package com.example.cm_yt.video.entity;

import com.example.cm_yt.script.entity.Script;
import com.example.cm_yt.user.entity.User;
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
@Table(name = "videos", indexes = {
    @Index(name = "idx_video_user", columnList = "user_id"),
    @Index(name = "idx_video_script", columnList = "script_id"),
    @Index(name = "idx_video_status", columnList = "status")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "script_id", nullable = false)
    private Script script;

    @Column(nullable = false, length = 200)
    private String title; // YouTube 업로드용 제목

    @Column(length = 5000)
    private String description; // YouTube 업로드용 설명

    @ElementCollection
    @CollectionTable(name = "video_tags", joinColumns = @JoinColumn(name = "video_id"))
    @Column(name = "tag", length = 50)
    private List<String> tags = new ArrayList<>(); // YouTube 업로드용 태그

    @Column(length = 500)
    private String thumbnailUrl; // 생성된 썸네일 URL

    @Column(length = 500)
    private String videoFileUrl; // 생성된 영상 파일 URL

    @Column
    private Integer videoDurationMs; // 영상 길이 (밀리초)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private VideoStatus status = VideoStatus.PENDING;

    @Column(length = 50)
    private String youtubeVideoId; // YouTube 업로드 후 비디오 ID

    @Column(length = 500)
    private String youtubeUrl; // YouTube 업로드 후 URL

    @Column
    private LocalDateTime uploadedAt; // YouTube 업로드 완료 시간

    @Column(length = 2000)
    private String errorMessage; // 실패 시 에러 메시지

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Video(User user, Script script, String title, String description, List<String> tags) {
        this.user = user;
        this.script = script;
        this.title = title;
        this.description = description;
        this.tags = tags != null ? tags : new ArrayList<>();
        this.status = VideoStatus.PENDING;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateThumbnail(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void updateStatus(VideoStatus status) {
        this.status = status;
    }

    public void updateVideoFile(String videoFileUrl, Integer videoDurationMs) {
        this.videoFileUrl = videoFileUrl;
        this.videoDurationMs = videoDurationMs;
        this.status = VideoStatus.GENERATED;
    }

    public void updateYoutubeInfo(String youtubeVideoId, String youtubeUrl) {
        this.youtubeVideoId = youtubeVideoId;
        this.youtubeUrl = youtubeUrl;
        this.uploadedAt = LocalDateTime.now();
        this.status = VideoStatus.UPLOADED;
    }

    public void markAsFailed(String errorMessage) {
        this.status = VideoStatus.FAILED;
        this.errorMessage = errorMessage;
    }

    public void addTag(String tag) {
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    public void removeTag(String tag) {
        this.tags.remove(tag);
    }
}
