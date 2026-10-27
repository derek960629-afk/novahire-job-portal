package com.novahire.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications", indexes = {
        @Index(name = "idx_applications_job", columnList = "job_id"),
        @Index(name = "idx_applications_email", columnList = "email")
})
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(length = 30)
    private String phone;

    @Column(name = "resume_url", length = 500)
    private String resumeUrl;

    @Column(name = "cover_letter", length = 2000)
    private String coverLetter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApplicationStatus status = ApplicationStatus.SUBMITTED;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected JobApplication() {
    }

    public JobApplication(Job job, String name, String email, String phone,
                          String resumeUrl, String coverLetter) {
        this.job = job;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.resumeUrl = resumeUrl;
        this.coverLetter = coverLetter;
        this.status = ApplicationStatus.SUBMITTED;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Job getJob() {
        return job;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
