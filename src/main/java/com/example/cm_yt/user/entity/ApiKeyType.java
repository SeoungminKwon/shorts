package com.example.cm_yt.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiKeyType {
    YOUTUBE("YouTube Data API", "YouTube"),
    LLM("Large Language Model API", "ChatGPT"),
    IMAGE_GENERATION("Image Generation API", "ChatGPT"),
    TTS("Text-to-Speech API", "Typecast");

    private final String description;
    private final String defaultProvider;
}
