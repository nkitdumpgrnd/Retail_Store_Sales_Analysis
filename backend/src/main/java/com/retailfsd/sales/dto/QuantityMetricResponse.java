package com.retailfsd.sales.dto;

public class QuantityMetricResponse {
    private String label;
    private Long quantity;

    public QuantityMetricResponse(String label, Long quantity) {
        this.label = label;
        this.quantity = quantity;
    }

    public String getLabel() { return label; }
    public Long getQuantity() { return quantity; }
}
