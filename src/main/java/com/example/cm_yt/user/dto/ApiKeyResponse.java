package com.example.cm_yt.user.dto;

import com.example.cm_yt.user.entity.ApiKey;
import com.example.cm_yt.user.entity.ApiKeyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ApiKeyResponse {

    private Long id;
    private ApiKeyType apiType;
    private String apiKeyMasked; // 보안을 위해 마스킹된 API 키
    private String provider;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ApiKeyResponse from(ApiKey apiKey) {
        return ApiKeyResponse.builder()
                .id(apiKey.getId())
                .apiType(apiKey.getApiType())
                .apiKeyMasked(maskApiKey(apiKey.getApiKey()))
                .provider(apiKey.getProvider())
                .description(apiKey.getDescription())
                .createdAt(apiKey.getCreatedAt())
                .updatedAt(apiKey.getUpdatedAt())
                .build();
    }

    /**
     * API 키를 마스킹 처리 (앞 4자리와 뒤 4자리만 보여주고 나머지는 *)
     */
    private static String maskApiKey(String apiKey) {
        if (apiKey == null || apiKey.length() <= 8) {
            return "****";
        }
        int length = apiKey.length();
        String prefix = apiKey.substring(0, 4);
        String suffix = apiKey.substring(length - 4);
        return prefix + "****" + suffix;
    }
}
