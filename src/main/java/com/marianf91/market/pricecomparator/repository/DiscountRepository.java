package com.marianf91.market.pricecomparator.repository;

import com.marianf91.market.pricecomparator.model.Discount;
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

    /**
     * Dynamically filter by any combination of:
     *   - exact store name (case-insensitive)
     *   - exact product ID
     *   - fromDate ≥ …
     *   - toDate   ≤ …
     */
    @Query("""
      SELECT d
        FROM Discount d
       WHERE (:storeName IS NULL OR LOWER(d.store.name) = LOWER(:storeName))
         AND (:productId IS NULL OR d.product.id = :productId)
         AND (:fromDate IS NULL OR d.fromDate >= :fromDate)
         AND (:toDate   IS NULL OR d.toDate   <= :toDate)
    """)
    List<Discount> findByFilters(
            @Param("storeName") String storeName,
            @Param("productId")  String productId,
            @Param("fromDate")   LocalDate fromDate,
            @Param("toDate")     LocalDate toDate
    );
}