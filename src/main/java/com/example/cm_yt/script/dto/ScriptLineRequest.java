package com.example.cm_yt.script.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScriptLineRequest {

    @NotNull(message = "줄 번호는 필수입니다")
    private Integer lineNumber;

    @NotBlank(message = "텍스트는 필수입니다")
    private String text;

    private Long characterId; // 등장 캐릭터 (선택적)
}
