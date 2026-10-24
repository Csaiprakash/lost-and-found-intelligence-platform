package com.lostfound.ReportService.repo;

import com.lostfound.ReportService.entity.Report;
import com.lostfound.ReportService.enums.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    // Get all reports created by a particular user
    List<Report> findByUserId(Long userId);

    // Filter by LOST or FOUND
    List<Report> findByType(ReportType type);

    // Filter by category, ignoring case
    List<Report> findByCategoryIgnoreCase(String category);

    // Filter by both type and category
    List<Report> findByTypeAndCategoryIgnoreCase(
            ReportType type,
            String category
    );

    // User-specific filtering
    List<Report> findByUserIdAndType(
            Long userId,
            ReportType type
    );

    List<Report> findByUserIdAndCategoryIgnoreCase(
            Long userId,
            String category
    );

    List<Report> findByUserIdAndTypeAndCategoryIgnoreCase(
            Long userId,
            ReportType type,
            String category
    );
}