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

    /** All prices */
    public List<PriceSnapshot> getAllPrices() {
        return repo.findAll();
    }

    /**
     * Filters prices using optional parameters.
     * If all parameters are null, returns repo.findAll().
     */
    public List<PriceSnapshot> getAllPrices(
            String storeName,
            String productId,
            LocalDate date
    ) {
        if (storeName == null && productId == null && date == null) {
            return getAllPrices();
        }
        return repo.findByFilters(
                storeName,
                productId,
                date
        );
    }
}
