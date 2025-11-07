package com.example.cm_yt.video.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VideoStatus {
    PENDING("대기 중", "영상 생성 대기 중"),
    GENERATING("생성 중", "영상 생성 진행 중"),
    GENERATED("생성 완료", "영상 파일 생성 완료"),
    UPLOADING("업로드 중", "YouTube에 업로드 중"),
    UPLOADED("업로드 완료", "YouTube 업로드 완료"),
    FAILED("실패", "영상 생성 또는 업로드 실패");

    private final String displayName;
    private final String description;
}
