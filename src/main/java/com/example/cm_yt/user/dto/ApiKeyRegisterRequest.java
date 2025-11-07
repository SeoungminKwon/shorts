package com.example.cm_yt.user.dto;

import com.example.cm_yt.user.entity.ApiKeyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiKeyRegisterRequest {

    @NotNull(message = "API 키 타입은 필수입니다")
    private ApiKeyType apiType;

    @NotBlank(message = "API 키는 필수입니다")
    private String apiKey;

    private String provider; // null이면 기본값 사용

    private String description;
}
