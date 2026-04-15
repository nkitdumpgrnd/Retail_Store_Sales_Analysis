package com.retailfsd.sales.service;

import com.retailfsd.sales.dto.DashboardSummary;
import com.retailfsd.sales.dto.MetricResponse;
import com.retailfsd.sales.dto.QuantityMetricResponse;
import com.retailfsd.sales.model.Sale;
import com.retailfsd.sales.repository.SaleRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {
    private final SaleRepository saleRepository;

    public AnalyticsService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public DashboardSummary summary() {
        List<MetricResponse> products = revenueByProduct();
        List<QuantityMetricResponse> quantities = quantityByProduct();
        List<MetricResponse> cities = revenueByCity();
        List<Sale> sales = saleRepository.findAll();
        MetricResponse highestOrder = sales.stream()
                .max(Comparator.comparing(Sale::getTotalSales))
                .map(sale -> new MetricResponse(String.valueOf(sale.getOrderId()), sale.getTotalSales()))
                .orElse(new MetricResponse("N/A", BigDecimal.ZERO));
        return new DashboardSummary(
                safe(saleRepository.totalRevenue()),
                saleRepository.count(),
                averagePrice(sales),
                products.isEmpty() ? new MetricResponse("N/A", BigDecimal.ZERO) : products.get(0),
                quantities.isEmpty() ? new QuantityMetricResponse("N/A", 0L) : quantities.get(0),
                cities.isEmpty() ? new MetricResponse("N/A", BigDecimal.ZERO) : cities.get(0),
                highestOrder);
    }

    public List<QuantityMetricResponse> quantityByProduct() {
        return saleRepository.quantityByProduct().stream()
                .map(row -> new QuantityMetricResponse((String) row[0], ((Number) row[1]).longValue()))
                .collect(Collectors.toList());
    }

    public List<MetricResponse> revenueByProduct() {
        return toMetrics(saleRepository.revenueByProduct());
    }

    public List<MetricResponse> revenueByCity() {
        return toMetrics(saleRepository.revenueByCity());
    }

    public List<MetricResponse> revenueByCategory() {
        return toMetrics(saleRepository.revenueByCategory());
    }

    public List<MetricResponse> revenueByDate() {
        return saleRepository.revenueByDate().stream()
                .map(row -> new MetricResponse(((LocalDate) row[0]).toString(), safe((BigDecimal) row[1])))
                .collect(Collectors.toList());
    }

    private List<MetricResponse> toMetrics(List<Object[]> rows) {
        return rows.stream()
                .map(row -> new MetricResponse(String.valueOf(row[0]), safe((BigDecimal) row[1])))
                .collect(Collectors.toList());
    }

    private BigDecimal safe(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private BigDecimal averagePrice(List<Sale> sales) {
        if (sales.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = sales.stream()
                .map(Sale::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.divide(BigDecimal.valueOf(sales.size()), 2, RoundingMode.HALF_UP);
    }
}
