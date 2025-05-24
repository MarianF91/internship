package com.marianf91.market.pricecomparator.service;

import com.marianf91.market.pricecomparator.repository.PriceSnapshotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PriceServiceTests {

    @Mock
    PriceSnapshotRepository repo;

    @InjectMocks
    PriceService service;

    @Test
    void whenNoData_thenEmptyList() {
        given(repo.findAll()).willReturn(List.of());
        assertTrue(service.getAllPrices().isEmpty());
    }
}
