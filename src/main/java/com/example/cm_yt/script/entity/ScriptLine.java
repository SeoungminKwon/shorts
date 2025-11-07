package com.example.cm_yt.script.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "script_lines", indexes = {
    @Index(name = "idx_script_line_number", columnList = "script_id, lineNumber")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScriptLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "script_id", nullable = false)
    private Script script;

    @Column(nullable = false)
    private Integer lineNumber; // 대본 줄 번호 (순서)

    @Column(nullable = false, length = 2000)
    private String text; // 대본 텍스트

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private Character character; // 등장하는 캐릭터 (선택적)

    @Column(length = 500)
    private String imageUrl; // 이 줄에 대해 생성된 이미지 URL

    @Column(length = 1000)
    private String imagePrompt; // 이미지 생성에 사용된 프롬프트

    @Column(length = 500)
    private String ttsAudioUrl; // 생성된 TTS 음성 파일 URL

    @Column
    private Integer audioDurationMs; // 음성 재생 시간 (밀리초)

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public ScriptLine(Script script, Integer lineNumber, String text, Character character) {
        this.script = script;
        this.lineNumber = lineNumber;
        this.text = text;
        this.character = character;
    }

    public void setScript(Script script) {
        this.script = script;
    }

    public void updateText(String text) {
        this.text = text;
    }

    public void updateLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void updateCharacter(Character character) {
        this.character = character;
    }

    public void updateImage(String imageUrl, String imagePrompt) {
        this.imageUrl = imageUrl;
        this.imagePrompt = imagePrompt;
    }

    public void updateTtsAudio(String ttsAudioUrl, Integer audioDurationMs) {
        this.ttsAudioUrl = ttsAudioUrl;
        this.audioDurationMs = audioDurationMs;
    }
}
