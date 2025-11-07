package com.example.cm_yt.script.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TtsGenerationRequest {

    private String voiceId; // TTS 음성 ID (선택적, 기본값 사용)

    private Float speed = 1.0f; // 재생 속도 (기본값 1.0)
}
