package com.retailfsd.sales.dto;

import java.math.BigDecimal;

public class MetricResponse {
    private String label;
    private BigDecimal value;

    public MetricResponse(String label, BigDecimal value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() { return label; }
    public BigDecimal getValue() { return value; }
}
