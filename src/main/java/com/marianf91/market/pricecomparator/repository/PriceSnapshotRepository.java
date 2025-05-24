package com.marianf91.market.pricecomparator.repository;

import com.marianf91.market.pricecomparator.model.PriceSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceSnapshotRepository extends JpaRepository<PriceSnapshot, Long> {
}