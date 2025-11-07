package com.example.cm_yt.user.controller;

import com.example.cm_yt.global.ApiResponse;
import com.example.cm_yt.user.dto.*;
import com.example.cm_yt.user.entity.ApiKey;
import com.example.cm_yt.user.entity.ApiKeyType;
import com.example.cm_yt.user.entity.User;
import com.example.cm_yt.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "User", description = "사용자 관리 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다")
    @PostMapping("/register")
    public ApiResponse<UserProfileResponse> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        User user = userService.registerUser(
                request.getEmail(),
                request.getPassword(),
                request.getUsername()
        );
        return ApiResponse.success(UserProfileResponse.from(user));
    }

    @Operation(summary = "사용자 정보 조회", description = "사용자 ID로 사용자 정보를 조회합니다")
    @GetMapping("/{userId}")
    public ApiResponse<UserProfileResponse> getUserProfile(@PathVariable Long userId) {
        User user = userService.findUserById(userId);
        return ApiResponse.success(UserProfileResponse.from(user));
    }

    @Operation(summary = "프로필 수정", description = "사용자 프로필(사용자명)을 수정합니다")
    @PutMapping("/{userId}/profile")
    public ApiResponse<UserProfileResponse> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateProfileRequest request) {
        User user = userService.updateUserProfile(userId, request.getUsername());
        return ApiResponse.success(UserProfileResponse.from(user));
    }

    @Operation(summary = "비밀번호 변경", description = "사용자 비밀번호를 변경합니다")
    @PutMapping("/{userId}/password")
    public ApiResponse<String> updatePassword(
            @PathVariable Long userId,
            @Valid @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(
                userId,
                request.getCurrentPassword(),
                request.getNewPassword()
        );
        return ApiResponse.success("비밀번호가 성공적으로 변경되었습니다");
    }

    @Operation(summary = "API 키 등록", description = "사용자의 API 키를 등록하거나 수정합니다")
    @PostMapping("/{userId}/api-keys")
    public ApiResponse<ApiKeyResponse> registerApiKey(
            @PathVariable Long userId,
            @Valid @RequestBody ApiKeyRegisterRequest request) {
        ApiKey apiKey = userService.registerOrUpdateApiKey(
                userId,
                request.getApiType(),
                request.getApiKey(),
                request.getProvider(),
                request.getDescription()
        );
        return ApiResponse.success(ApiKeyResponse.from(apiKey));
    }

    @Operation(summary = "모든 API 키 조회", description = "사용자의 모든 API 키를 조회합니다 (마스킹 처리됨)")
    @GetMapping("/{userId}/api-keys")
    public ApiResponse<List<ApiKeyResponse>> getAllApiKeys(@PathVariable Long userId) {
        List<ApiKey> apiKeys = userService.findAllApiKeys(userId);
        List<ApiKeyResponse> responses = apiKeys.stream()
                .map(ApiKeyResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success(responses);
    }

    @Operation(summary = "특정 API 키 조회", description = "사용자의 특정 타입 API 키를 조회합니다 (마스킹 처리됨)")
    @GetMapping("/{userId}/api-keys/{apiType}")
    public ApiResponse<ApiKeyResponse> getApiKey(
            @PathVariable Long userId,
            @PathVariable ApiKeyType apiType) {
        ApiKey apiKey = userService.findApiKey(userId, apiType);
        return ApiResponse.success(ApiKeyResponse.from(apiKey));
    }

    @Operation(summary = "API 키 삭제", description = "사용자의 특정 타입 API 키를 삭제합니다")
    @DeleteMapping("/{userId}/api-keys/{apiType}")
    public ApiResponse<String> deleteApiKey(
            @PathVariable Long userId,
            @PathVariable ApiKeyType apiType) {
        userService.deleteApiKey(userId, apiType);
        return ApiResponse.success("API 키가 성공적으로 삭제되었습니다");
    }
}
