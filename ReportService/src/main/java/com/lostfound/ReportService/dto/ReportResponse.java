package com.lostfound.ReportService.dto;

import com.lostfound.ReportService.entity.Report;
import com.lostfound.ReportService.enums.ReportStatus;
import com.lostfound.ReportService.enums.ReportType;

import java.time.LocalDateTime;

public record ReportResponse(
        Long id,
        Long userId,
        ReportType type,
        String title,
        String description,
        String category,
        String color,
        String distinctiveFeatures,
        String locationText,
        Double latitude,
        Double longitude,
        LocalDateTime eventTime,
        ReportStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ReportResponse fromEntity(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getUserId(),
                report.getType(),
                report.getTitle(),
                report.getDescription(),
                report.getCategory(),
                report.getColor(),
                report.getDistinctiveFeatures(),
                report.getLocationText(),
                report.getLatitude(),
                report.getLongitude(),
                report.getEventTime(),
                report.getStatus(),
                report.getCreatedAt(),
                report.getUpdatedAt()
        );
    }
}
