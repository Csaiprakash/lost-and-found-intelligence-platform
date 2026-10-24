package com.lostfound.MatchingService.service;

import com.lostfound.MatchingService.dto.MatchingReportCache;
import com.lostfound.MatchingService.entity.MatchingReport;
import com.lostfound.MatchingService.repo.MatchingReportRepository;
import com.lostfound.MatchingService.service.MatchingSearchService;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportMatchingService {

    private final VectorStore vectorStore;

    private final MatchingReportRepository matchingReportRepository;
    private final MatchingSearchService matchingSearchService;

    public ReportMatchingService(
            VectorStore vectorStore,
            MatchingReportRepository matchingReportRepository,MatchingSearchService matchingSearchService) {

        this.vectorStore = vectorStore;
        this.matchingReportRepository = matchingReportRepository;
        this.matchingSearchService=matchingSearchService;
    }

    @Cacheable(
            cacheNames = "matchingReports",
            key = "#reportId",
            unless = "#result == null"
    )
    public List<MatchingReportCache> findMatchingReportsCached(
            Long reportId) {

        return findMatchingReportsFromVectorStore(reportId)
                .stream()
                .map(document -> new MatchingReportCache(
                        document.getText(),
                        document.getMetadata()
                ))
                .toList();
    }

    public List<Document> findMatchingReports(Long reportId) {

        return findMatchingReportsCached(reportId)
                .stream()
                .map(cache -> new Document(
                        cache.getText(),
                        cache.getMetadata()
                ))
                .toList();
    }

    private List<Document> findMatchingReportsFromVectorStore(
            Long reportId) {
        return matchingSearchService
                .findMatchingReportsFromVectorStore(reportId);
    }

    private String valueOrDefault(String value) {
        return value == null ? "" : value;
    }
}
