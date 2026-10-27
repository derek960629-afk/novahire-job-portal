package com.novahire.service;

import com.novahire.domain.Favorite;
import com.novahire.repository.FavoriteRepository;
import com.novahire.repository.JobRepository;
import com.novahire.web.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final JobRepository jobRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, JobRepository jobRepository) {
        this.favoriteRepository = favoriteRepository;
        this.jobRepository = jobRepository;
    }

    @Transactional(readOnly = true)
    public List<Long> list(String sessionId) {
        return favoriteRepository.findBySessionIdOrderByCreatedAtDesc(sessionId).stream()
                .map(Favorite::getJobId)
                .toList();
    }

    @Transactional
    public void add(String sessionId, Long jobId) {
        if (!jobRepository.existsById(jobId)) {
            throw new ResourceNotFoundException("职位不存在或已下线");
        }
        if (!favoriteRepository.existsBySessionIdAndJobId(sessionId, jobId)) {
            favoriteRepository.save(new Favorite(sessionId, jobId));
        }
    }

    @Transactional
    public void remove(String sessionId, Long jobId) {
        favoriteRepository.deleteBySessionIdAndJobId(sessionId, jobId);
    }
}
