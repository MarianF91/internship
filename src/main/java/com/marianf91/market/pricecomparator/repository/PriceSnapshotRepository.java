package com.marianf91.market.pricecomparator.repository;

import com.marianf91.market.pricecomparator.model.PriceSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PriceSnapshotRepository extends JpaRepository<PriceSnapshot, Long> {

    /**
     * Dynamically filter by any combination of:
     *   - exact store name (case-insensitive)
     *   - exact product ID
     *   - exact snapshot date
     */
    @Query("""
      SELECT p
        FROM PriceSnapshot p
       WHERE (:storeName IS NULL OR LOWER(p.store.name) = LOWER(:storeName))
         AND (:productId IS NULL OR p.product.id = :productId)
         AND (:date      IS NULL OR p.date       = :date)
    """)
    List<PriceSnapshot> findByFilters(
            @Param("storeName") String storeName,
            @Param("productId")  String productId,
            @Param("date")       LocalDate date
    );
}