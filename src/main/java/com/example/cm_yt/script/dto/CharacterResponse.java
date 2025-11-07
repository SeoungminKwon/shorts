package com.example.cm_yt.script.dto;

import com.example.cm_yt.script.entity.Character;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CharacterResponse {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String seedValue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CharacterResponse from(Character character) {
        return CharacterResponse.builder()
                .id(character.getId())
                .name(character.getName())
                .description(character.getDescription())
                .imageUrl(character.getImageUrl())
                .seedValue(character.getSeedValue())
                .createdAt(character.getCreatedAt())
                .updatedAt(character.getUpdatedAt())
                .build();
    }
}
