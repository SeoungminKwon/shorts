package com.example.cm_yt.script.entity;

import com.example.cm_yt.shorts.entity.Shorts;
import com.example.cm_yt.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "scripts", indexes = {
    @Index(name = "idx_script_user", columnList = "user_id"),
    @Index(name = "idx_script_shorts", columnList = "shorts_id"),
    @Index(name = "idx_script_status", columnList = "status")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Script {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shorts_id", nullable = false)
    private Shorts shorts; // 원본 쇼츠

    @Column(nullable = false, length = 200)
    private String title; // 대본 제목

    @Column(length = 10000)
    private String originalText; // 원본 쇼츠의 스크립트 (참고용)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ScriptStatus status = ScriptStatus.DRAFT;

    @Column(length = 2000)
    private String memo; // 작업 메모

    @OneToMany(mappedBy = "script", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("lineNumber ASC")
    private List<ScriptLine> lines = new ArrayList<>();

    @OneToMany(mappedBy = "script", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Character> characters = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Script(User user, Shorts shorts, String title, String originalText) {
        this.user = user;
        this.shorts = shorts;
        this.title = title;
        this.originalText = originalText;
        this.status = ScriptStatus.DRAFT;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateStatus(ScriptStatus status) {
        this.status = status;
    }

    public void updateMemo(String memo) {
        this.memo = memo;
    }

    public void addLine(ScriptLine line) {
        this.lines.add(line);
        line.setScript(this);
    }

    public void removeLine(ScriptLine line) {
        this.lines.remove(line);
        line.setScript(null);
    }

    public void addCharacter(Character character) {
        this.characters.add(character);
        character.setScript(this);
    }

    public void removeCharacter(Character character) {
        this.characters.remove(character);
        character.setScript(null);
    }

    /**
     * 모든 ScriptLine의 텍스트를 하나의 문자열로 결합
     */
    public String getFullText() {
        return lines.stream()
                .map(ScriptLine::getText)
                .reduce("", (a, b) -> a + "\n" + b)
                .trim();
    }

    /**
     * 이미지 생성이 완료된 라인 수
     */
    public long getImageGeneratedCount() {
        return lines.stream()
                .filter(line -> line.getImageUrl() != null)
                .count();
    }

    /**
     * TTS 생성이 완료된 라인 수
     */
    public long getTtsGeneratedCount() {
        return lines.stream()
                .filter(line -> line.getTtsAudioUrl() != null)
                .count();
    }

    /**
     * 전체 진행률 계산 (이미지 + TTS 생성 기준)
     */
    public double getCompletionRate() {
        if (lines.isEmpty()) {
            return 0.0;
        }
        long totalTasks = lines.size() * 2L; // 각 라인당 이미지 + TTS
        long completedTasks = getImageGeneratedCount() + getTtsGeneratedCount();
        return (double) completedTasks / totalTasks * 100.0;
    }
}
