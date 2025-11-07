package com.example.cm_yt.script.dto;

import com.example.cm_yt.script.entity.ScriptLine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ScriptLineResponse {

    private Long id;
    private Integer lineNumber;
    private String text;
    private Long characterId;
    private String characterName;
    private String imageUrl;
    private String imagePrompt;
    private String ttsAudioUrl;
    private Integer audioDurationMs;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ScriptLineResponse from(ScriptLine line) {
        return ScriptLineResponse.builder()
                .id(line.getId())
                .lineNumber(line.getLineNumber())
                .text(line.getText())
                .characterId(line.getCharacter() != null ? line.getCharacter().getId() : null)
                .characterName(line.getCharacter() != null ? line.getCharacter().getName() : null)
                .imageUrl(line.getImageUrl())
                .imagePrompt(line.getImagePrompt())
                .ttsAudioUrl(line.getTtsAudioUrl())
                .audioDurationMs(line.getAudioDurationMs())
                .createdAt(line.getCreatedAt())
                .updatedAt(line.getUpdatedAt())
                .build();
    }
}
