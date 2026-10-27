package com.novahire.repository;

import com.novahire.domain.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByJobIdOrderByCreatedAtDesc(Long jobId);

    boolean existsByJobIdAndEmailIgnoreCase(Long jobId, String email);
}
