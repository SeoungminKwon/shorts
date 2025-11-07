package com.example.cm_yt.script.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CharacterRequest {

    @NotBlank(message = "캐릭터 이름은 필수입니다")
    private String name;

    private String description; // 캐릭터 특징 설명
}
