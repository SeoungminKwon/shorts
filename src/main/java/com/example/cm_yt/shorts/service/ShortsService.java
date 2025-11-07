package com.example.cm_yt.shorts.service;

import com.example.cm_yt.shorts.dto.ShortsSearchRequest;
import com.example.cm_yt.shorts.entity.Shorts;
import com.example.cm_yt.shorts.entity.ShortsCategory;
import com.example.cm_yt.shorts.repository.ShortsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShortsService {

    private final ShortsRepository shortsRepository;

    /**
     * 쇼츠 등록
     */
    @Transactional
    public Shorts registerShorts(String videoId, String title, String description,
                                  String thumbnailUrl, String videoUrl, String channelId,
                                  String channelTitle, LocalDateTime publishedAt,
                                  Long viewCount, Long likeCount, Long channelSubscriberCount,
                                  ShortsCategory category, List<String> tags) {
        // 중복 체크
        if (shortsRepository.existsByVideoId(videoId)) {
            throw new IllegalArgumentException("이미 등록된 쇼츠입니다: " + videoId);
        }

        Shorts shorts = Shorts.builder()
                .videoId(videoId)
                .title(title)
                .description(description)
                .thumbnailUrl(thumbnailUrl)
                .videoUrl(videoUrl)
                .channelId(channelId)
                .channelTitle(channelTitle)
                .publishedAt(publishedAt)
                .viewCount(viewCount)
                .likeCount(likeCount)
                .channelSubscriberCount(channelSubscriberCount)
                .category(category)
                .tags(tags)
                .build();

        return shortsRepository.save(shorts);
    }

    /**
     * 쇼츠 검색
     */
    public Page<Shorts> searchShorts(ShortsSearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        // 키워드 검색이 있는 경우
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            return shortsRepository.searchByKeyword(request.getKeyword(), pageable);
        }

        // 태그 검색이 있는 경우
        if (request.getTag() != null && !request.getTag().isEmpty()) {
            return shortsRepository.findByTag(request.getTag(), pageable);
        }

        // 카테고리가 지정되지 않은 경우
        if (request.getCategory() == null) {
            throw new IllegalArgumentException("카테고리를 선택해주세요");
        }

        // 정렬 타입에 따라 다른 검색 수행
        switch (request.getSortType()) {
            case VIEW_TO_SUBSCRIBER_RATIO:
                // 조회수/구독자 비율 순 (구독자 적고 조회수 많은 순)
                return shortsRepository.findByCategoryOrderByViewToSubscriberRatioDesc(
                        request.getCategory(), pageable);

            case VIEW_COUNT:
                // 조회수 순
                return shortsRepository.findByCategoryOrderByViewCountDesc(
                        request.getCategory(), pageable);

            case LATEST:
                // 최신 등록 순
                pageable = PageRequest.of(request.getPage(), request.getSize(),
                        Sort.by(Sort.Direction.DESC, "createdAt"));
                return shortsRepository.findByCategory(request.getCategory(), pageable);

            default:
                throw new IllegalArgumentException("지원하지 않는 정렬 방식입니다");
        }
    }

    /**
     * ID로 쇼츠 조회
     */
    public Shorts findById(Long id) {
        return shortsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("쇼츠를 찾을 수 없습니다: " + id));
    }

    /**
     * 비디오 ID로 쇼츠 조회
     */
    public Shorts findByVideoId(String videoId) {
        return shortsRepository.findByVideoId(videoId)
                .orElseThrow(() -> new IllegalArgumentException("쇼츠를 찾을 수 없습니다: " + videoId));
    }

    /**
     * 쇼츠 통계 업데이트 (조회수, 좋아요 수)
     */
    @Transactional
    public Shorts updateStatistics(Long id, Long viewCount, Long likeCount) {
        Shorts shorts = findById(id);
        shorts.updateStatistics(viewCount, likeCount);
        return shorts;
    }

    /**
     * 쇼츠 메모 업데이트
     */
    @Transactional
    public Shorts updateMemo(Long id, String memo) {
        Shorts shorts = findById(id);
        shorts.updateMemo(memo);
        return shorts;
    }

    /**
     * 쇼츠 태그 추가
     */
    @Transactional
    public Shorts addTag(Long id, String tag) {
        Shorts shorts = findById(id);
        shorts.addTag(tag);
        return shorts;
    }

    /**
     * 쇼츠 태그 삭제
     */
    @Transactional
    public Shorts removeTag(Long id, String tag) {
        Shorts shorts = findById(id);
        shorts.removeTag(tag);
        return shorts;
    }

    /**
     * 최근 등록된 쇼츠 조회
     */
    public List<Shorts> getRecentShorts() {
        return shortsRepository.findTop10ByOrderByCreatedAtDesc();
    }
}
