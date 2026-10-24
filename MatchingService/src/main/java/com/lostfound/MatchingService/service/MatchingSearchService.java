package com.lostfound.MatchingService.service;

import com.lostfound.MatchingService.entity.MatchingReport;
import com.lostfound.MatchingService.repo.MatchingReportRepository;
import com.lostfound.MatchingService.exception.MatchingSearchUnavailableException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchingSearchService {

    private final VectorStore vectorStore;

    private final MatchingReportRepository matchingReportRepository;

    public MatchingSearchService(
            VectorStore vectorStore,
            MatchingReportRepository matchingReportRepository) {

        this.vectorStore = vectorStore;
        this.matchingReportRepository = matchingReportRepository;
    }

    @CircuitBreaker(
            name = "matchingSearch",
            fallbackMethod = "matchingSearchFallback"
    )
    public List<Document> findMatchingReportsFromVectorStore(
            Long reportId) {

        // Step 1: Retrieve original report
        MatchingReport report =
                matchingReportRepository.findById(reportId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Report not found: " + reportId
                                ));

        // Step 2: Convert report into searchable text
        String query = """
                Report Type: %s
                Title: %s
                Description: %s
                Category: %s
                Location: %s
                """.formatted(
                valueOrDefault(report.getType()),
                valueOrDefault(report.getTitle()),
                valueOrDefault(report.getDescription()),
                valueOrDefault(report.getCategory()),
                valueOrDefault(report.getLocationText())
        );

        // Step 3: Similarity search request
        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(10)
                .similarityThreshold(0.70)
                .build();

        // Step 4: Search PGVector
        List<Document> documents =
                vectorStore.similaritySearch(searchRequest);

        // Step 5: Filter original report and same type
        return documents.stream()
                .filter(document -> {

                    Object metadataReportId =
                            document.getMetadata().get("reportId");

                    return metadataReportId != null
                            && !reportId.toString()
                            .equals(metadataReportId.toString());
                })
                .filter(document -> {

                    Object metadataType =
                            document.getMetadata().get("type");

                    return metadataType != null
                            && !valueOrDefault(report.getType())
                            .equalsIgnoreCase(
                                    metadataType.toString()
                            );
                })
                .toList();
    }

    public List<Document> matchingSearchFallback(
            Long reportId,
            Throwable exception) {

        throw new MatchingSearchUnavailableException(
                "Matching search is temporarily unavailable",
                exception
        );
    }

    private String valueOrDefault(String value) {
        return value == null ? "" : value;
    }
}
