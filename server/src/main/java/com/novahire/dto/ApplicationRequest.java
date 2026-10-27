package com.novahire.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ApplicationRequest(
        @NotNull Long jobId,
        @NotBlank @Size(max = 60) String name,
        @NotBlank @Email @Size(max = 120) String email,
        @Size(max = 30) String phone,
        @Size(max = 500) String resumeUrl,
        @Size(max = 2000) String coverLetter
) {
}
