package com.example.cm_yt.script.repository;

import com.example.cm_yt.script.entity.Script;
import com.example.cm_yt.script.entity.ScriptStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScriptRepository extends JpaRepository<Script, Long> {

    /**
     * 사용자의 모든 스크립트 조회
     */
    Page<Script> findByUserId(Long userId, Pageable pageable);

    /**
     * 사용자의 특정 상태 스크립트 조회
     */
    Page<Script> findByUserIdAndStatus(Long userId, ScriptStatus status, Pageable pageable);

    /**
     * 특정 쇼츠로부터 생성된 모든 스크립트 조회
     */
    List<Script> findByShortsId(Long shortsId);

    /**
     * 사용자의 최근 스크립트 조회
     */
    List<Script> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
}
