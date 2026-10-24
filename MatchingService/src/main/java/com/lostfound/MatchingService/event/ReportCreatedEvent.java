package com.lostfound.MatchingService.event;
import java.time.Instant;

public record ReportCreatedEvent(
        String eventId,
        String eventType,
        Long reportId,
        Long userId,
        String type,
        String title,
        String description,
        String category,
        String locationText,
        Instant occurredAt
) {
}
