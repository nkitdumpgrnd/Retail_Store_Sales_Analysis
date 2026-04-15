package com.retailfsd.sales.service;

import com.retailfsd.sales.model.Sale;
import com.retailfsd.sales.repository.SaleRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AnalyticsServiceTest {
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private AnalyticsService analyticsService;

    @BeforeEach
    void setUp() {
        saleRepository.deleteAll();
        saleRepository.saveAll(Arrays.asList(
                sale(1L, "Laptop", "Pune", "Electronics", 2, "50000"),
                sale(2L, "Mobile", "Mumbai", "Electronics", 3, "20000"),
                sale(3L, "Keyboard", "Nagpur", "Accessories", 4, "1500")
        ));
    }

    @Test
    void calculatesTotalRevenueAndTopCity() {
        Assertions.assertEquals(new BigDecimal("166000.00"), analyticsService.summary().getTotalRevenue().setScale(2));
        Assertions.assertEquals("Pune", analyticsService.summary().getTopCity().getLabel());
    }

    private Sale sale(Long id, String product, String city, String category, int quantity, String price) {
        Sale sale = new Sale();
        sale.setOrderId(id);
        sale.setOrderDate(LocalDate.of(2025, 1, 10));
        sale.setProduct(product);
        sale.setCity(city);
        sale.setCategory(category);
        sale.setQuantity(quantity);
        sale.setPrice(new BigDecimal(price));
        return sale;
    }
}
