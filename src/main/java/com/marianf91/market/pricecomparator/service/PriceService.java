package com.marianf91.market.pricecomparator.service;

import com.marianf91.market.pricecomparator.model.PriceSnapshot;
import com.marianf91.market.pricecomparator.repository.PriceSnapshotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceService {
    private final PriceSnapshotRepository repo;

    public PriceService(PriceSnapshotRepository repo) {
        this.repo = repo;
    }

    public List<PriceSnapshot> getAllPrices() {
        return repo.findAll();
    }
}