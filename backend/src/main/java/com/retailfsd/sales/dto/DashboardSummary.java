package com.retailfsd.sales.dto;

import java.math.BigDecimal;

public class DashboardSummary {
    private BigDecimal totalRevenue;
    private long totalOrders;
    private BigDecimal averagePrice;
    private MetricResponse topRevenueProduct;
    private QuantityMetricResponse topSellingProduct;
    private MetricResponse topCity;
    private MetricResponse highestValueOrder;

    public DashboardSummary(BigDecimal totalRevenue, long totalOrders, BigDecimal averagePrice,
                            MetricResponse topRevenueProduct, QuantityMetricResponse topSellingProduct,
                            MetricResponse topCity, MetricResponse highestValueOrder) {
        this.totalRevenue = totalRevenue;
        this.totalOrders = totalOrders;
        this.averagePrice = averagePrice;
        this.topRevenueProduct = topRevenueProduct;
        this.topSellingProduct = topSellingProduct;
        this.topCity = topCity;
        this.highestValueOrder = highestValueOrder;
    }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public long getTotalOrders() { return totalOrders; }
    public BigDecimal getAveragePrice() { return averagePrice; }
    public MetricResponse getTopRevenueProduct() { return topRevenueProduct; }
    public QuantityMetricResponse getTopSellingProduct() { return topSellingProduct; }
    public MetricResponse getTopCity() { return topCity; }
    public MetricResponse getHighestValueOrder() { return highestValueOrder; }
}
