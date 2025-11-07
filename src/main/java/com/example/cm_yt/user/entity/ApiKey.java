package com.example.cm_yt.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "api_keys", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "api_type"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "api_type", nullable = false, length = 30)
    private ApiKeyType apiType;

    @Column(nullable = false, length = 500)
    private String apiKey;

    @Column(nullable = false, length = 50)
    private String provider;

    @Column(length = 1000)
    private String description;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public ApiKey(User user, ApiKeyType apiType, String apiKey, String provider, String description) {
        this.user = user;
        this.apiType = apiType;
        this.apiKey = apiKey;
        this.provider = provider != null ? provider : apiType.getDefaultProvider();
        this.description = description;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void updateApiKey(String newApiKey) {
        this.apiKey = newApiKey;
    }

    public void updateProvider(String newProvider) {
        this.provider = newProvider;
    }

    public void updateDescription(String newDescription) {
        this.description = newDescription;
    }
}
