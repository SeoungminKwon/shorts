package com.example.cm_yt.user.service;

import com.example.cm_yt.user.entity.ApiKey;
import com.example.cm_yt.user.entity.ApiKeyType;
import com.example.cm_yt.user.entity.User;
import com.example.cm_yt.user.repository.ApiKeyRepository;
import com.example.cm_yt.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final ApiKeyRepository apiKeyRepository;

    /**
     * 회원가입
     * TODO: 비밀번호 암호화 (BCryptPasswordEncoder 사용)
     */
    @Transactional
    public User registerUser(String email, String password, String username) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + email);
        }

        // TODO: 비밀번호 암호화
        // String encodedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .email(email)
                .password(password) // TODO: encodedPassword로 변경
                .username(username)
                .build();

        return userRepository.save(user);
    }

    /**
     * 이메일로 사용자 조회
     */
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + email));
    }

    /**
     * ID로 사용자 조회
     */
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
    }

    /**
     * 사용자 프로필 수정
     */
    @Transactional
    public User updateUserProfile(Long userId, String username) {
        User user = findUserById(userId);
        user.updateProfile(username);
        return user;
    }

    /**
     * 비밀번호 변경
     * TODO: 비밀번호 암호화 및 현재 비밀번호 검증
     */
    @Transactional
    public void updatePassword(Long userId, String currentPassword, String newPassword) {
        User user = findUserById(userId);

        // TODO: 현재 비밀번호 검증
        // if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
        //     throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다");
        // }

        // TODO: 새 비밀번호 암호화
        // String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.updatePassword(newPassword); // TODO: encodedNewPassword로 변경
    }

    /**
     * API 키 등록 또는 수정
     */
    @Transactional
    public ApiKey registerOrUpdateApiKey(Long userId, ApiKeyType apiType, String apiKey, String provider, String description) {
        User user = findUserById(userId);

        // 기존 API 키가 있으면 수정, 없으면 생성
        return apiKeyRepository.findByUserIdAndApiType(userId, apiType)
                .map(existingKey -> {
                    existingKey.updateApiKey(apiKey);
                    if (provider != null) {
                        existingKey.updateProvider(provider);
                    }
                    if (description != null) {
                        existingKey.updateDescription(description);
                    }
                    return existingKey;
                })
                .orElseGet(() -> {
                    ApiKey newKey = ApiKey.builder()
                            .user(user)
                            .apiType(apiType)
                            .apiKey(apiKey)
                            .provider(provider)
                            .description(description)
                            .build();
                    user.addApiKey(newKey);
                    return apiKeyRepository.save(newKey);
                });
    }

    /**
     * 사용자의 모든 API 키 조회
     */
    public List<ApiKey> findAllApiKeys(Long userId) {
        return apiKeyRepository.findByUserId(userId);
    }

    /**
     * 특정 타입의 API 키 조회
     */
    public ApiKey findApiKey(Long userId, ApiKeyType apiType) {
        return apiKeyRepository.findByUserIdAndApiType(userId, apiType)
                .orElseThrow(() -> new IllegalArgumentException(
                        "API 키를 찾을 수 없습니다. Type: " + apiType));
    }

    /**
     * API 키 삭제
     */
    @Transactional
    public void deleteApiKey(Long userId, ApiKeyType apiType) {
        if (!apiKeyRepository.existsByUserIdAndApiType(userId, apiType)) {
            throw new IllegalArgumentException("삭제할 API 키를 찾을 수 없습니다. Type: " + apiType);
        }
        apiKeyRepository.deleteByUserIdAndApiType(userId, apiType);
    }
}
