package com.example.cm_yt.user.repository;

import com.example.cm_yt.user.entity.ApiKey;
import com.example.cm_yt.user.entity.ApiKeyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

    /**
     * 특정 사용자의 모든 API 키 조회
     */
    List<ApiKey> findByUserId(Long userId);

    /**
     * 특정 사용자의 특정 타입 API 키 조회
     */
    Optional<ApiKey> findByUserIdAndApiType(Long userId, ApiKeyType apiType);

    /**
     * 특정 사용자의 특정 타입 API 키 존재 여부 확인
     */
    boolean existsByUserIdAndApiType(Long userId, ApiKeyType apiType);

    /**
     * 특정 사용자의 API 키 삭제
     */
    void deleteByUserIdAndApiType(Long userId, ApiKeyType apiType);
}
