package com.novahire.dto;

public record CompanyDto(
        Long id,
        String name,
        String industry,
        String size,
        String color,
        long count
) {
}
