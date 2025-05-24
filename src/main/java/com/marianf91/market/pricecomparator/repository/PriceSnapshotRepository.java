package com.marianf91.market.pricecomparator.repository;

import com.marianf91.market.pricecomparator.model.PriceSnapshot;
import com.marianf91.market.pricecomparator.model.Product;
import com.marianf91.market.pricecomparator.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PriceSnapshotRepository extends JpaRepository<PriceSnapshot, Long> {
    List<PriceSnapshot> findByStore(Store store);
    List<PriceSnapshot> findByProduct(Product product);
    List<PriceSnapshot> findByDate(LocalDate date);
}
