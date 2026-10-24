package com.lostfound.ReportService.controller;

import com.lostfound.ReportService.dto.CreateReportRequest;
import com.lostfound.ReportService.dto.ReportResponse;
import com.lostfound.ReportService.dto.UpdateReportRequest;
import com.lostfound.ReportService.enums.ReportType;
import com.lostfound.ReportService.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReportResponse createReport(
            @Valid @RequestBody CreateReportRequest request
    ) {
        return reportService.createReport(request);
    }

    @GetMapping("/{id}")
    public ReportResponse getReportById(@PathVariable Long id) {
        return reportService.getReportById(id);
    }

    @GetMapping("/my")
    public List<ReportResponse> getMyReports(
            @RequestParam Long userId
    ) {
        return reportService.getMyReports(userId);
    }

    @GetMapping
    public List<ReportResponse> searchReports(
            @RequestParam(required = false) ReportType type,
            @RequestParam(required = false) String category
    ) {
        return reportService.searchReports(type, category);
    }

    @PatchMapping("/{id}")
    public ReportResponse updateReport(
            @PathVariable Long id,
            @RequestParam Long userId,
            @Valid @RequestBody UpdateReportRequest request
    ) {
        return reportService.updateReport(id, userId, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReport(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        reportService.deleteReport(id, userId);
    }
}
