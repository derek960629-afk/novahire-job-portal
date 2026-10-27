package com.novahire.dto;

import java.time.LocalDateTime;

public record ApplicationDto(
        Long id,
        Long jobId,
        String jobTitle,
        String company,
        String name,
        String email,
        String phone,
        String resumeUrl,
        String coverLetter,
        String status,
        LocalDateTime createdAt
) {
}
