package com.lostfound.ReportService.producer;

import com.lostfound.ReportService.event.ReportCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReportEventProducer {

    private static final Logger log =
            LoggerFactory.getLogger(ReportEventProducer.class);

    private static final String TOPIC = "report-created";

    private final KafkaTemplate<String, ReportCreatedEvent> kafkaTemplate;

    public ReportEventProducer(
            KafkaTemplate<String, ReportCreatedEvent> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishReportCreatedEvent(ReportCreatedEvent event) {

        String key = String.valueOf(event.reportId());

        kafkaTemplate.send(TOPIC, key, event)
                .whenComplete((result, exception) -> {
                    if (exception == null) {
                        log.info(
                                "ReportCreated event published. reportId={}, topic={}, partition={}",
                                event.reportId(),
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition()
                        );
                    } else {
                        log.error(
                                "Failed to publish ReportCreated event for reportId={}",
                                event.reportId(),
                                exception
                        );
                    }
                });
    }
}
