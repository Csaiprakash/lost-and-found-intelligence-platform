package com.lostfound.MatchingService.repo;

import com.lostfound.MatchingService.entity.MatchingReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchingReportRepository
        extends JpaRepository<MatchingReport, Long> {

    List<MatchingReport> findByType(String type);

    List<MatchingReport> findByTypeAndCategory(
            String type,
            String category
    );
}
