package com.example.cm_yt.video.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VideoCreateRequest {

    @NotNull(message = "스크립트 ID는 필수입니다")
    private Long scriptId;

    @NotBlank(message = "제목은 필수입니다")
    private String title;

    private String description;

    private List<String> tags;
}
