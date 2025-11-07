package com.example.cm_yt.script.dto;

import com.example.cm_yt.script.entity.Script;
import com.example.cm_yt.script.entity.ScriptStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ScriptResponse {

    private Long id;
    private Long userId;
    private Long shortsId;
    private String shortsTitle;
    private String title;
    private ScriptStatus status;
    private Integer totalLines;
    private Long imageGeneratedCount;
    private Long ttsGeneratedCount;
    private Double completionRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ScriptResponse from(Script script) {
        return ScriptResponse.builder()
                .id(script.getId())
                .userId(script.getUser().getId())
                .shortsId(script.getShorts().getId())
                .shortsTitle(script.getShorts().getTitle())
                .title(script.getTitle())
                .status(script.getStatus())
                .totalLines(script.getLines().size())
                .imageGeneratedCount(script.getImageGeneratedCount())
                .ttsGeneratedCount(script.getTtsGeneratedCount())
                .completionRate(script.getCompletionRate())
                .createdAt(script.getCreatedAt())
                .updatedAt(script.getUpdatedAt())
                .build();
    }
}
