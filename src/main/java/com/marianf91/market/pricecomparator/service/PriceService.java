package com.marianf91.market.pricecomparator.service;

import com.marianf91.market.pricecomparator.model.PriceSnapshot;
import com.marianf91.market.pricecomparator.repository.PriceSnapshotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PriceService {
    private final PriceSnapshotRepository repo;

    public PriceService(PriceSnapshotRepository repo) {
        this.repo = repo;
    }

    /**
     * No filters ⇒ return everything
     */
    public List<PriceSnapshot> getAllPrices() {
        return repo.findAll();
    }

    /**
     * Optional filters ⇒ delegate to our single JPQL method
     */
    public List<PriceSnapshot> getAllPrices(
            String storeName,
            String productId,
            LocalDate date
    ) {
        return repo.findByFilters(storeName, productId, date);
    }
}
