package com.retailfsd.sales.config;

import com.retailfsd.sales.model.Sale;
import com.retailfsd.sales.model.UserAccount;
import com.retailfsd.sales.repository.SaleRepository;
import com.retailfsd.sales.repository.UserAccountRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seedData(SaleRepository saleRepository, UserAccountRepository userRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                UserAccount admin = new UserAccount();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ROLE_ADMIN");
                userRepository.save(admin);
            }
            if (saleRepository.count() == 0) {
                saleRepository.saveAll(Arrays.asList(
                        sale(1001L, "2025-01-10", "Pune", "Laptop", "Electronics", 2, "50000"),
                        sale(1002L, "2025-01-11", "Mumbai", "Mobile", "Electronics", 3, "20000"),
                        sale(1003L, "2025-01-11", "Pune", "Headphones", "Accessories", 5, "2000"),
                        sale(1004L, "2025-01-12", "Nagpur", "Keyboard", "Accessories", 4, "1500"),
                        sale(1005L, "2025-01-13", "Mumbai", "Laptop", "Electronics", 1, "50000"),
                        sale(1006L, "2025-01-14", "Nagpur", "Mobile", "Electronics", 2, "20000"),
                        sale(1007L, "2025-01-15", "Pune", "Keyboard", "Accessories", 3, "1500"),
                        sale(1008L, "2025-01-16", "Mumbai", "Headphones", "Accessories", 8, "2000")
                ));
            }
        };
    }

    private Sale sale(Long id, String date, String city, String product, String category, int quantity, String price) {
        Sale sale = new Sale();
        sale.setOrderId(id);
        sale.setOrderDate(LocalDate.parse(date));
        sale.setCity(city);
        sale.setProduct(product);
        sale.setCategory(category);
        sale.setQuantity(quantity);
        sale.setPrice(new BigDecimal(price));
        return sale;
    }
}
