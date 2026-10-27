package com.novahire.service;

import com.novahire.domain.Job;
import com.novahire.domain.JobApplication;
import com.novahire.dto.ApplicationDto;
import com.novahire.dto.ApplicationRequest;
import com.novahire.repository.JobApplicationRepository;
import com.novahire.repository.JobRepository;
import com.novahire.web.ConflictException;
import com.novahire.web.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final JobRepository jobRepository;

    public ApplicationService(JobApplicationRepository applicationRepository, JobRepository jobRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
    }

    @Transactional
    public ApplicationDto apply(ApplicationRequest request) {
        Job job = jobRepository.findById(request.jobId())
                .filter(Job::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("职位不存在或已下线"));

        String email = request.email().trim();
        if (applicationRepository.existsByJobIdAndEmailIgnoreCase(job.getId(), email)) {
            throw new ConflictException("该邮箱已投递过该职位，请勿重复投递");
        }

        JobApplication application = new JobApplication(
                job,
                request.name().trim(),
                email,
                trimToNull(request.phone()),
                trimToNull(request.resumeUrl()),
                trimToNull(request.coverLetter())
        );
        return toDto(applicationRepository.save(application));
    }

    @Transactional(readOnly = true)
    public List<ApplicationDto> listByJob(Long jobId) {
        if (!jobRepository.existsById(jobId)) {
            throw new ResourceNotFoundException("职位不存在");
        }
        return applicationRepository.findByJobIdOrderByCreatedAtDesc(jobId).stream()
                .map(this::toDto)
                .toList();
    }

    private ApplicationDto toDto(JobApplication application) {
        Job job = application.getJob();
        return new ApplicationDto(
                application.getId(),
                job.getId(),
                job.getTitle(),
                job.getCompany().getName(),
                application.getName(),
                application.getEmail(),
                application.getPhone(),
                application.getResumeUrl(),
                application.getCoverLetter(),
                application.getStatus().name(),
                application.getCreatedAt()
        );
    }

    private String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
