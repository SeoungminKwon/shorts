package com.example.cm_yt.script.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageGenerationRequest {

    @NotBlank(message = "이미지 생성 프롬프트는 필수입니다")
    private String prompt;

    private Long characterId; // 특정 캐릭터 지정 시 시드값 사용
}
