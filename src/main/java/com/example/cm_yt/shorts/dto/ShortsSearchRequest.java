package com.example.cm_yt.shorts.dto;

import com.example.cm_yt.shorts.entity.ShortsCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShortsSearchRequest {

    private ShortsCategory category;

    private SortType sortType = SortType.VIEW_TO_SUBSCRIBER_RATIO; // 기본값: 조회수/구독자 비율

    private String keyword; // 제목/설명 검색

    private String tag; // 태그 검색

    private Integer page = 0; // 페이지 번호 (기본값 0)

    private Integer size = 20; // 페이지 크기 (기본값 20)

    public enum SortType {
        VIEW_TO_SUBSCRIBER_RATIO, // 조회수/구독자 비율 (구독자 적고 조회수 많은 순)
        VIEW_COUNT, // 조회수 순
        LATEST // 최신 등록 순
    }
}
