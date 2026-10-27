package com.novahire.dto;

import java.time.LocalDateTime;
import java.util.List;

public record JobDto(
        Long id,
        String title,
        String company,
        String color,
        int salaryMin,
        int salaryMax,
        String salaryText,
        String city,
        String exp,
        String edu,
        String type,
        List<String> tags,
        String industry,
        String size,
        double days,
        boolean hot,
        boolean favorite,
        String description,
        List<String> requirements,
        LocalDateTime postedAt
) {
}
