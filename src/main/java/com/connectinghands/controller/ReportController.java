package com.connectinghands.controller;

import com.connectinghands.dto.DonationReportDto;
import com.connectinghands.dto.ResourceUtilizationReportDto;
import com.connectinghands.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/donations/{orphanageId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORPHANAGE')")
    public ResponseEntity<DonationReportDto> getDonationReport(
            @PathVariable Long orphanageId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "DETAILED") String reportType,
            @RequestParam(defaultValue = "USD") String currency,
            @RequestParam(defaultValue = "UTC") String timeZone) {
        
        return ResponseEntity.ok(reportService.generateDonationReport(
                orphanageId, startDate, endDate, reportType, currency, timeZone));
    }

    @GetMapping("/donations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<DonationReportDto>> getAllDonationReports(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "SUMMARY") String reportType,
            @RequestParam(defaultValue = "USD") String currency,
            @RequestParam(defaultValue = "UTC") String timeZone,
            Pageable pageable) {
        
        return ResponseEntity.ok(reportService.generateDonationReports(
                startDate, endDate, reportType, currency, timeZone, pageable));
    }

    @GetMapping("/resources/{orphanageId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORPHANAGE')")
    public ResponseEntity<ResourceUtilizationReportDto> getResourceUtilizationReport(
            @PathVariable Long orphanageId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "DETAILED") String reportType,
            @RequestParam(defaultValue = "UTC") String timeZone) {
        
        return ResponseEntity.ok(reportService.generateResourceUtilizationReport(
                orphanageId, startDate, endDate, reportType, timeZone));
    }

    @GetMapping("/resources")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ResourceUtilizationReportDto>> getAllResourceUtilizationReports(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "SUMMARY") String reportType,
            @RequestParam(defaultValue = "UTC") String timeZone,
            Pageable pageable) {
        
        return ResponseEntity.ok(reportService.generateResourceUtilizationReports(
                startDate, endDate, reportType, timeZone, pageable));
    }

    @GetMapping("/donations/{orphanageId}/export")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORPHANAGE')")
    public ResponseEntity<byte[]> exportDonationReport(
            @PathVariable Long orphanageId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "DETAILED") String reportType,
            @RequestParam(defaultValue = "USD") String currency,
            @RequestParam(defaultValue = "UTC") String timeZone,
            @RequestParam(defaultValue = "PDF") String format) {
        
        byte[] reportData = reportService.exportDonationReport(
                orphanageId, startDate, endDate, reportType, currency, timeZone, format);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(getMediaType(format));
        headers.setContentDispositionFormData("attachment", 
                String.format("donation-report-%d.%s", orphanageId, format.toLowerCase()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(reportData);
    }

    @GetMapping("/resources/{orphanageId}/export")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORPHANAGE')")
    public ResponseEntity<byte[]> exportResourceUtilizationReport(
            @PathVariable Long orphanageId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "DETAILED") String reportType,
            @RequestParam(defaultValue = "UTC") String timeZone,
            @RequestParam(defaultValue = "PDF") String format) {
        
        byte[] reportData = reportService.exportResourceUtilizationReport(
                orphanageId, startDate, endDate, reportType, timeZone, format);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(getMediaType(format));
        headers.setContentDispositionFormData("attachment", 
                String.format("resource-utilization-report-%d.%s", orphanageId, format.toLowerCase()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(reportData);
    }

    private MediaType getMediaType(String format) {
        return switch (format.toUpperCase()) {
            case "PDF" -> MediaType.APPLICATION_PDF;
            case "CSV" -> MediaType.parseMediaType("text/csv");
            case "EXCEL" -> MediaType.parseMediaType("application/vnd.ms-excel");
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }
}
