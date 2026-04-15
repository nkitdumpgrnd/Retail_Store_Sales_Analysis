package com.retailfsd.sales.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "sales")
public class Sale {
    @Id
    private Long orderId;
    @NotNull
    private LocalDate orderDate;
    @NotBlank
    private String city;
    @NotBlank
    private String product;
    @NotBlank
    private String category;
    @Min(1)
    private Integer quantity;
    @NotNull
    @DecimalMin("0.0")
    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getTotalSales() {
        return price == null || quantity == null ? BigDecimal.ZERO : price.multiply(BigDecimal.valueOf(quantity));
    }
}
