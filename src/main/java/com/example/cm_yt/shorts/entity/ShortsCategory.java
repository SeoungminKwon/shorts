package com.example.cm_yt.shorts.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShortsCategory {
    HUMOR("유머", "재미있는 콘텐츠"),
    TRAVEL("여행", "여행 관련 콘텐츠"),
    POLITICS("정치", "정치 관련 콘텐츠"),
    ECONOMY("경제", "경제/비즈니스 콘텐츠"),
    SPORTS("스포츠", "스포츠 콘텐츠"),
    MUSIC("음악", "음악 관련 콘텐츠"),
    GAMING("게임", "게임 콘텐츠"),
    EDUCATION("교육", "교육/학습 콘텐츠"),
    FOOD("음식", "요리/먹방 콘텐츠"),
    FASHION("패션", "패션/뷰티 콘텐츠"),
    TECHNOLOGY("기술", "IT/기술 콘텐츠"),
    DAILY("일상", "일상 브이로그"),
    OTHER("기타", "기타 콘텐츠");

    private final String displayName;
    private final String description;
}
