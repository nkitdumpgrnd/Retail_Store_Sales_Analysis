package com.retailfsd.sales.repository;

import com.retailfsd.sales.model.Sale;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByCityIgnoreCase(String city);

    @Query("select coalesce(sum(s.quantity * s.price), 0) from Sale s")
    BigDecimal totalRevenue();

    @Query("select s.product, sum(s.quantity) from Sale s group by s.product order by sum(s.quantity) desc")
    List<Object[]> quantityByProduct();

    @Query("select s.product, sum(s.quantity * s.price) from Sale s group by s.product order by sum(s.quantity * s.price) desc")
    List<Object[]> revenueByProduct();

    @Query("select s.city, sum(s.quantity * s.price) from Sale s group by s.city order by sum(s.quantity * s.price) desc")
    List<Object[]> revenueByCity();

    @Query("select s.category, sum(s.quantity * s.price) from Sale s group by s.category order by sum(s.quantity * s.price) desc")
    List<Object[]> revenueByCategory();

    @Query("select s.orderDate, sum(s.quantity * s.price) from Sale s group by s.orderDate order by s.orderDate")
    List<Object[]> revenueByDate();
}
