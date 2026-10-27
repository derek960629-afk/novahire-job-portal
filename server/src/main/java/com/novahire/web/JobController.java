package com.novahire.web;

import com.novahire.dto.JobCreateRequest;
import com.novahire.dto.JobDto;
import com.novahire.service.JobService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public List<JobDto> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String exp,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String salary,
            @RequestParam(defaultValue = "recommend") String sort,
            HttpServletRequest request) {
        return jobService.search(keyword, city, exp, type, salary, sort, sessionId(request));
    }

    @GetMapping("/{id}")
    public JobDto detail(@PathVariable Long id, HttpServletRequest request) {
        return jobService.get(id, sessionId(request));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobDto create(@Valid @RequestBody JobCreateRequest request) {
        return jobService.create(request);
    }

    private String sessionId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : session.getId();
    }
}
