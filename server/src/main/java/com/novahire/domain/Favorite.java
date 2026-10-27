package com.novahire.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorites",
        uniqueConstraints = @UniqueConstraint(name = "uk_favorites_session_job", columnNames = {"session_id", "job_id"}),
        indexes = @Index(name = "idx_favorites_session", columnList = "session_id"))
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false, length = 100)
    private String sessionId;

    @Column(name = "job_id", nullable = false)
    private Long jobId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected Favorite() {
    }

    public Favorite(String sessionId, Long jobId) {
        this.sessionId = sessionId;
        this.jobId = jobId;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Long getJobId() {
        return jobId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
