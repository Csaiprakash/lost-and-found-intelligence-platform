package com.lostfound.ReportService.dto;

import com.lostfound.ReportService.enums.ReportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateReportRequest(

        @NotNull
        Long userId,

        @NotNull
        ReportType type,

        @NotBlank
        String title,

        String description,
        String category,
        String color,
        String distinctiveFeatures,
        String locationText,
        Double latitude,
        Double longitude,
        LocalDateTime eventTime
) {
}
