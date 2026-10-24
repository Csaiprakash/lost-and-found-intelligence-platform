package com.lostfound.ReportService.dto;

import com.lostfound.ReportService.enums.ReportStatus;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record UpdateReportRequest(

        @Size(min = 1, message = "Title cannot be blank")
        String title,

        String description,
        String category,
        String color,
        String distinctiveFeatures,
        String locationText,
        Double latitude,
        Double longitude,
        LocalDateTime eventTime,
        ReportStatus status
) {
}
