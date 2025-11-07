package com.example.cm_yt.script.dto;

import com.example.cm_yt.script.entity.Script;
import com.example.cm_yt.script.entity.ScriptStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class ScriptDetailResponse {

    private Long id;
    private Long userId;
    private Long shortsId;
    private String shortsTitle;
    private String title;
    private String originalText;
    private ScriptStatus status;
    private String memo;
    private List<ScriptLineResponse> lines;
    private List<CharacterResponse> characters;
    private Double completionRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ScriptDetailResponse from(Script script) {
        return ScriptDetailResponse.builder()
                .id(script.getId())
                .userId(script.getUser().getId())
                .shortsId(script.getShorts().getId())
                .shortsTitle(script.getShorts().getTitle())
                .title(script.getTitle())
                .originalText(script.getOriginalText())
                .status(script.getStatus())
                .memo(script.getMemo())
                .lines(script.getLines().stream()
                        .map(ScriptLineResponse::from)
                        .collect(Collectors.toList()))
                .characters(script.getCharacters().stream()
                        .map(CharacterResponse::from)
                        .collect(Collectors.toList()))
                .completionRate(script.getCompletionRate())
                .createdAt(script.getCreatedAt())
                .updatedAt(script.getUpdatedAt())
                .build();
    }
}
