package com.example.cm_yt.shorts.dto;

import com.example.cm_yt.shorts.entity.ShortsCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShortsRegisterRequest {

    @NotBlank(message = "비디오 ID는 필수입니다")
    private String videoId;

    @NotBlank(message = "제목은 필수입니다")
    private String title;

    private String description;

    @NotBlank(message = "썸네일 URL은 필수입니다")
    private String thumbnailUrl;

    @NotBlank(message = "비디오 URL은 필수입니다")
    private String videoUrl;

    @NotBlank(message = "채널 ID는 필수입니다")
    private String channelId;

    @NotBlank(message = "채널명은 필수입니다")
    private String channelTitle;

    @NotNull(message = "업로드 날짜는 필수입니다")
    private LocalDateTime publishedAt;

    private Long viewCount;

    private Long likeCount;

    private Long channelSubscriberCount;

    @NotNull(message = "카테고리는 필수입니다")
    private ShortsCategory category;

    private List<String> tags;
}
