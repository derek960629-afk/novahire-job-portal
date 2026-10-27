package com.novahire.web;

import com.novahire.dto.ApplicationDto;
import com.novahire.dto.ApplicationRequest;
import com.novahire.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationDto apply(@Valid @RequestBody ApplicationRequest request) {
        return applicationService.apply(request);
    }

    @GetMapping
    public List<ApplicationDto> listByJob(@RequestParam Long jobId) {
        return applicationService.listByJob(jobId);
    }
}
