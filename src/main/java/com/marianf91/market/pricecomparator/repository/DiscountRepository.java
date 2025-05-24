package com.marianf91.market.pricecomparator.repository;

import com.marianf91.market.pricecomparator.model.Discount;
import com.marianf91.market.pricecomparator.model.Product;
import com.marianf91.market.pricecomparator.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    @Query("""
           SELECT d.product AS product,
                  MAX(d.percentage) AS maxPct
             FROM Discount d
            WHERE :today BETWEEN d.fromDate AND d.toDate
            GROUP BY d.product
           """)
    List<Object[]> findBestCurrentDiscounts(@Param("today") LocalDate today);

    List<Discount> findByStore(Store store);
    List<Discount> findByProduct(Product product);
    List<Discount> findByFromDateBetween(LocalDate from, LocalDate to);
}