package com.lostfound.MatchingService.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lostfound.MatchingService.event.ReportCreatedEvent;
import com.lostfound.MatchingService.entity.MatchingReport;
import com.lostfound.MatchingService.repo.MatchingReportRepository;
import com.lostfound.MatchingService.service.ReportEmbeddingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ReportCreatedConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(ReportCreatedConsumer.class);

    private final ObjectMapper objectMapper;
    private final MatchingReportRepository matchingReportRepository;
    private final ReportEmbeddingService reportEmbeddingService;

    public ReportCreatedConsumer(
            ObjectMapper objectMapper,
            MatchingReportRepository matchingReportRepository,
            ReportEmbeddingService reportEmbeddingService) {

        this.objectMapper = objectMapper;
        this.matchingReportRepository = matchingReportRepository;
        this.reportEmbeddingService = reportEmbeddingService;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.topic-name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(String message) {

        try {
            ReportCreatedEvent event =
                    objectMapper.readValue(message, ReportCreatedEvent.class);

            // Step 1: Convert Kafka event into MatchingReport
            MatchingReport report = new MatchingReport();

            report.setId(event.reportId());
            report.setUserId(event.userId());
            report.setType(event.type());
            report.setTitle(event.title());
            report.setDescription(event.description());
            report.setCategory(event.category());
            report.setLocationText(event.locationText());

            // Step 2: Save report in the relational database
            matchingReportRepository.save(report);

            log.info(
                    "Report stored in Matching Service database. reportId={}",
                    event.reportId()
            );

            // Step 3: Generate and save the report embedding
            reportEmbeddingService.saveReportEmbedding(
                    event.reportId(),
                    event.userId(),
                    event.type(),
                    event.title(),
                    event.description(),
                    event.category(),
                    event.locationText()
            );

            log.info(
                    "Report embedding saved successfully. reportId={}",
                    event.reportId()
            );

        } catch (Exception exception) {
            log.error(
                    "Failed to process ReportCreated message: {}",
                    message,
                    exception
            );
        }
    }
}