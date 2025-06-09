package com.connectinghands.service.impl;

import com.connectinghands.dto.DonationReportDto;
import com.connectinghands.dto.ResourceUtilizationReportDto;
import com.connectinghands.entity.Donation;
import com.connectinghands.entity.Orphanage;
import com.connectinghands.entity.Resource;
import com.connectinghands.entity.ResourceRequest;
import com.connectinghands.repository.DonationRepository;
import com.connectinghands.repository.OrphanageRepository;
import com.connectinghands.repository.ResourceRepository;
import com.connectinghands.repository.ResourceRequestRepository;
import com.connectinghands.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final DonationRepository donationRepository;
    private final ResourceRepository resourceRepository;
    private final ResourceRequestRepository resourceRequestRepository;
    private final OrphanageRepository orphanageRepository;

    @Override
    @Transactional(readOnly = true)
    public DonationReportDto generateDonationReport(
            Long orphanageId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String reportType,
            String currency,
            String timeZone) {
        
        Orphanage orphanage = orphanageRepository.findById(orphanageId)
                .orElseThrow(() -> new RuntimeException("Orphanage not found"));

        List<Donation> donations = donationRepository.findByOrphanageIdAndTimestampBetween(
                orphanageId, startDate, endDate);

        DonationReportDto report = new DonationReportDto();
        report.setOrphanageId(orphanageId);
        report.setOrphanageName(orphanage.getName());
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        report.setReportType(reportType);
        report.setCurrency(currency);
        report.setTimeZone(timeZone);

        // Calculate totals
        report.setTotalDonations((long) donations.size());
        report.setTotalMonetaryAmount(calculateTotalMonetaryAmount(donations, currency));
        report.setTotalResourceDonations(calculateTotalResourceDonations(donations));

        return report;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DonationReportDto> generateDonationReports(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String reportType,
            String currency,
            String timeZone,
            Pageable pageable) {
        
        List<Orphanage> orphanages = orphanageRepository.findAll(pageable).getContent();
        List<DonationReportDto> reports = orphanages.stream()
                .map(orphanage -> generateDonationReport(
                        orphanage.getId(), startDate, endDate, reportType, currency, timeZone))
                .collect(Collectors.toList());

        return new PageImpl<>(reports, pageable, orphanages.size());
    }

    @Override
    @Transactional(readOnly = true)
    public ResourceUtilizationReportDto generateResourceUtilizationReport(
            Long orphanageId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String reportType,
            String timeZone) {
        
        Orphanage orphanage = orphanageRepository.findById(orphanageId)
                .orElseThrow(() -> new RuntimeException("Orphanage not found"));

        List<Resource> resources = resourceRepository.findByOrphanageId(orphanageId);
        List<ResourceRequest> requests = resourceRequestRepository.findByOrphanageIdAndTimestampBetween(
                orphanageId, startDate, endDate);

        ResourceUtilizationReportDto report = new ResourceUtilizationReportDto();
        report.setOrphanageId(orphanageId);
        report.setOrphanageName(orphanage.getName());
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        report.setReportType(reportType);
        report.setTimeZone(timeZone);

        // Calculate resource utilization
        Map<String, Long> utilization = calculateResourceUtilization(resources);
        report.setResourceUtilization(utilization);

        // Calculate resource requests
        Map<String, Long> requestCounts = calculateResourceRequests(requests);
        report.setResourceRequests(requestCounts);

        // Calculate resource donations
        Map<String, Long> donationCounts = calculateResourceDonations(resources);
        report.setResourceDonations(donationCounts);

        // Identify low stock resources
        report.setLowStockResources(identifyLowStockResources(resources));

        // Identify high demand resources
        report.setHighDemandResources(identifyHighDemandResources(requestCounts));

        return report;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResourceUtilizationReportDto> generateResourceUtilizationReports(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String reportType,
            String timeZone,
            Pageable pageable) {
        
        List<Orphanage> orphanages = orphanageRepository.findAll(pageable).getContent();
        List<ResourceUtilizationReportDto> reports = orphanages.stream()
                .map(orphanage -> generateResourceUtilizationReport(
                        orphanage.getId(), startDate, endDate, reportType, timeZone))
                .collect(Collectors.toList());

        return new PageImpl<>(reports, pageable, orphanages.size());
    }

    @Override
    public byte[] exportDonationReport(
            Long orphanageId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String reportType,
            String currency,
            String timeZone,
            String format) {
        // TODO: Implement export functionality
        throw new UnsupportedOperationException("Export functionality not implemented yet");
    }

    @Override
    public byte[] exportResourceUtilizationReport(
            Long orphanageId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String reportType,
            String timeZone,
            String format) {
        // TODO: Implement export functionality
        throw new UnsupportedOperationException("Export functionality not implemented yet");
    }

    private BigDecimal calculateTotalMonetaryAmount(List<Donation> donations, String currency) {
        return donations.stream()
                .filter(donation -> donation.getAmount() != null)
                .map(Donation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Long calculateTotalResourceDonations(List<Donation> donations) {
        return donations.stream()
                .filter(donation -> donation.getResource() != null)
                .count();
    }

    private Map<String, Long> calculateResourceUtilization(List<Resource> resources) {
        return resources.stream()
                .collect(Collectors.toMap(
                        Resource::getName,
                        resource -> resource.getQuantity() - resource.getCurrentQuantity()
                ));
    }

    private Map<String, Long> calculateResourceRequests(List<ResourceRequest> requests) {
        return requests.stream()
                .collect(Collectors.groupingBy(
                        request -> request.getResource().getName(),
                        Collectors.counting()
                ));
    }

    private Map<String, Long> calculateResourceDonations(List<Resource> resources) {
        return resources.stream()
                .collect(Collectors.toMap(
                        Resource::getName,
                        resource -> resource.getQuantity() - resource.getCurrentQuantity()
                ));
    }

    private List<String> identifyLowStockResources(List<Resource> resources) {
        return resources.stream()
                .filter(resource -> resource.getCurrentQuantity() <= resource.getMinimumQuantity())
                .map(Resource::getName)
                .collect(Collectors.toList());
    }

    private List<String> identifyHighDemandResources(Map<String, Long> requestCounts) {
        return requestCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
} 