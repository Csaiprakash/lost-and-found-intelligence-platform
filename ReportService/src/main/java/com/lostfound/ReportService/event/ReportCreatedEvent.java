package com.lostfound.ReportService.event;

import com.lostfound.ReportService.entity.Report;
import com.lostfound.ReportService.enums.ReportType;

import java.time.Instant;
import java.util.UUID;

public record ReportCreatedEvent(
        String eventId,
        String eventType,
        Long reportId,
        Long userId,
        ReportType type,
        String title,
        String description,
        String category,
        String locationText,
        Instant occurredAt
) {
    public static ReportCreatedEvent fromReport(Report report) {
        return new ReportCreatedEvent(
                UUID.randomUUID().toString(),
                "REPORT_CREATED",
                report.getId(),
                report.getUserId(),
                report.getType(),
                report.getTitle(),
                report.getDescription(),
                report.getCategory(),
                report.getLocationText(),
                Instant.now()
        );
    }
}

