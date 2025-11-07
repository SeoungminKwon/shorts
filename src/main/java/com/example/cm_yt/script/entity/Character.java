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
@Table(name = "characters")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "script_id", nullable = false)
    private Script script;

    @Column(nullable = false, length = 50)
    private String name; // 캐릭터 이름

    @Column(length = 1000)
    private String description; // 캐릭터 특징/설명

    @Column(length = 500)
    private String imageUrl; // 생성된 캐릭터 이미지 URL

    @Column(length = 100)
    private String seedValue; // 이미지 생성 시드값 (동일 인물 유지용)

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Character(Script script, String name, String description, String imageUrl, String seedValue) {
        this.script = script;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.seedValue = seedValue;
    }

    public void setScript(Script script) {
        this.script = script;
    }

    public void updateCharacter(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void updateSeedValue(String seedValue) {
        this.seedValue = seedValue;
    }
}
