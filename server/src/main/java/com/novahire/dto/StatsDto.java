package com.novahire.dto;

public record StatsDto(
        long companies,
        long jobs,
        int responseHours,
        long placements
) {
}
