package com.novahire.service;

import com.novahire.dto.StatsDto;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

    private static final int RESPONSE_HOURS = 2;
    private static final long PLACEMENTS = 86_000L;

    private final CompanyService companyService;
    private final JobService jobService;

    public StatsService(CompanyService companyService, JobService jobService) {
        this.companyService = companyService;
        this.jobService = jobService;
    }

    public StatsDto current() {
        return new StatsDto(companyService.count(), jobService.count(), RESPONSE_HOURS, PLACEMENTS);
    }
}
