package com.novahire.repository;

import com.novahire.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findBySessionIdOrderByCreatedAtDesc(String sessionId);

    boolean existsBySessionIdAndJobId(String sessionId, Long jobId);

    long deleteBySessionIdAndJobId(String sessionId, Long jobId);
}
