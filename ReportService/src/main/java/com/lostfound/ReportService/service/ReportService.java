package com.lostfound.ReportService.service;

import com.lostfound.ReportService.dto.CreateReportRequest;
import com.lostfound.ReportService.dto.ReportResponse;
import com.lostfound.ReportService.dto.UpdateReportRequest;
import com.lostfound.ReportService.entity.Report;
import com.lostfound.ReportService.enums.ReportStatus;
import com.lostfound.ReportService.enums.ReportType;
import com.lostfound.ReportService.repo.ReportRepository;
import com.lostfound.ReportService.event.ReportCreatedEvent;
import com.lostfound.ReportService.producer.ReportEventProducer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReportEventProducer reportEventProducer;

    public ReportService(
            ReportRepository reportRepository,
            ReportEventProducer reportEventProducer
    ) {
        this.reportRepository = reportRepository;
        this.reportEventProducer = reportEventProducer;
    }

    public ReportResponse createReport(CreateReportRequest request) {
        Report report = new Report();

        report.setUserId(request.userId());
        report.setType(request.type());
        report.setTitle(request.title());
        report.setDescription(request.description());
        report.setCategory(request.category());
        report.setColor(request.color());
        report.setDistinctiveFeatures(request.distinctiveFeatures());
        report.setLocationText(request.locationText());
        report.setLatitude(request.latitude());
        report.setLongitude(request.longitude());
        report.setEventTime(request.eventTime());
        report.setStatus(ReportStatus.ACTIVE);

        Report savedReport = reportRepository.save(report);
        ReportCreatedEvent event =
                ReportCreatedEvent.fromReport(savedReport);

        reportEventProducer.publishReportCreatedEvent(event);

        return ReportResponse.fromEntity(savedReport);
    }

    public ReportResponse getReportById(Long reportId) {
        Report report = findReport(reportId);
        return ReportResponse.fromEntity(report);
    }

    public List<ReportResponse> getMyReports(Long userId) {
        return reportRepository.findByUserId(userId)
                .stream()
                .map(ReportResponse::fromEntity)
                .toList();
    }

    public List<ReportResponse> searchReports(
            ReportType type,
            String category
    ) {
        List<Report> reports;

        if (type != null && category != null && !category.isBlank()) {
            reports = reportRepository
                    .findByTypeAndCategoryIgnoreCase(type, category);
        } else if (type != null) {
            reports = reportRepository.findByType(type);
        } else if (category != null && !category.isBlank()) {
            reports = reportRepository.findByCategoryIgnoreCase(category);
        } else {
            reports = reportRepository.findAll();
        }

        return reports.stream()
                .map(ReportResponse::fromEntity)
                .toList();
    }

    public ReportResponse updateReport(
            Long reportId,
            Long userId,
            UpdateReportRequest request
    ) {
        Report report = findReport(reportId);
        verifyOwner(report, userId);

        if (request.title() != null) {
            report.setTitle(request.title());
        }
        if (request.description() != null) {
            report.setDescription(request.description());
        }
        if (request.category() != null) {
            report.setCategory(request.category());
        }
        if (request.color() != null) {
            report.setColor(request.color());
        }
        if (request.distinctiveFeatures() != null) {
            report.setDistinctiveFeatures(request.distinctiveFeatures());
        }
        if (request.locationText() != null) {
            report.setLocationText(request.locationText());
        }
        if (request.latitude() != null) {
            report.setLatitude(request.latitude());
        }
        if (request.longitude() != null) {
            report.setLongitude(request.longitude());
        }
        if (request.eventTime() != null) {
            report.setEventTime(request.eventTime());
        }
        if (request.status() != null) {
            report.setStatus(request.status());
        }

        Report updatedReport = reportRepository.save(report);

        return ReportResponse.fromEntity(updatedReport);
    }

    public void deleteReport(Long reportId, Long userId) {
        Report report = findReport(reportId);
        verifyOwner(report, userId);

        reportRepository.delete(report);
    }

    private Report findReport(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Report not found with id: " + reportId
                ));
    }

    private void verifyOwner(Report report, Long userId) {
        if (!report.getUserId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to modify this report"
            );
        }
    }
}
