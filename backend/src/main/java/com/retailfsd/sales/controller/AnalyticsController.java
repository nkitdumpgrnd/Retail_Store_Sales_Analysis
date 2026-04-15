package com.retailfsd.sales.controller;

import com.retailfsd.sales.dto.DashboardSummary;
import com.retailfsd.sales.dto.MetricResponse;
import com.retailfsd.sales.dto.QuantityMetricResponse;
import com.retailfsd.sales.service.AnalyticsService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/summary")
    public DashboardSummary summary() { return analyticsService.summary(); }
    @GetMapping("/products/revenue")
    public List<MetricResponse> revenueByProduct() { return analyticsService.revenueByProduct(); }
    @GetMapping("/products/quantity")
    public List<QuantityMetricResponse> quantityByProduct() { return analyticsService.quantityByProduct(); }
    @GetMapping("/cities/revenue")
    public List<MetricResponse> revenueByCity() { return analyticsService.revenueByCity(); }
    @GetMapping("/categories/revenue")
    public List<MetricResponse> revenueByCategory() { return analyticsService.revenueByCategory(); }
    @GetMapping("/dates/revenue")
    public List<MetricResponse> revenueByDate() { return analyticsService.revenueByDate(); }
}
