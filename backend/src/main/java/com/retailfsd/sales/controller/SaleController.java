package com.retailfsd.sales.controller;

import com.retailfsd.sales.model.Sale;
import com.retailfsd.sales.repository.SaleRepository;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
    private final SaleRepository saleRepository;

    public SaleController(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @GetMapping
    public List<Sale> getSales(@RequestParam(required = false) String city) {
        return city == null || city.trim().isEmpty() ? saleRepository.findAll() : saleRepository.findByCityIgnoreCase(city);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Sale> getSale(@PathVariable Long orderId) {
        return saleRepository.findById(orderId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Sale> createSale(@Valid @RequestBody Sale sale) {
        if (saleRepository.existsById(sale.getOrderId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Sale saved = saleRepository.save(sale);
        return ResponseEntity.created(URI.create("/api/sales/" + saved.getOrderId())).body(saved);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Sale> updateSale(@PathVariable Long orderId, @Valid @RequestBody Sale sale) {
        if (!saleRepository.existsById(orderId)) {
            return ResponseEntity.notFound().build();
        }
        sale.setOrderId(orderId);
        return ResponseEntity.ok(saleRepository.save(sale));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long orderId) {
        if (!saleRepository.existsById(orderId)) {
            return ResponseEntity.notFound().build();
        }
        saleRepository.deleteById(orderId);
        return ResponseEntity.noContent().build();
    }
}
