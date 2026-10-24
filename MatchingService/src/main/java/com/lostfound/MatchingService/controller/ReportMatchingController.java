package com.lostfound.MatchingService.controller;

import com.lostfound.MatchingService.service.ReportMatchingService;

import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matching")
public class ReportMatchingController {

    private final ReportMatchingService reportMatchingService;

    public ReportMatchingController(
            ReportMatchingService reportMatchingService) {

        this.reportMatchingService = reportMatchingService;
    }

    @GetMapping("/{reportId}")
    public List<Document> findMatchingReports(
            @PathVariable Long reportId) {

        return reportMatchingService.findMatchingReports(reportId);
    }
}
