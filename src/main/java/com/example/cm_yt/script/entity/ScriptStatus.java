package com.example.cm_yt.script.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScriptStatus {
    DRAFT("초안", "LLM으로 각색 중이거나 사용자가 검토 중인 상태"),
    CONFIRMED("확정", "사용자가 대본을 확정한 상태"),
    IMAGE_GENERATING("이미지 생성 중", "이미지 생성 API 호출 중"),
    TTS_GENERATING("음성 생성 중", "TTS API 호출 중"),
    COMPLETED("완료", "이미지와 음성 생성이 모두 완료된 상태"),
    VIDEO_GENERATING("영상 생성 중", "최종 영상 생성 중");

    private final String displayName;
    private final String description;
}
