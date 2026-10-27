package com.novahire.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record JobCreateRequest(
        @NotBlank @Size(max = 120) String title,
        @NotNull Long companyId,
        @Min(0) int salaryMin,
        @Min(0) int salaryMax,
        @NotBlank @Size(max = 60) String salaryText,
        @NotBlank @Size(max = 40) String city,
        @NotBlank @Size(max = 20) String exp,
        @NotBlank @Size(max = 20) String education,
        @NotBlank @Size(max = 20) String type,
        List<@Size(max = 50) String> tags,
        List<@Size(max = 500) String> requirements,
        @NotBlank @Size(max = 4000) String description,
        boolean hot
) {
}
