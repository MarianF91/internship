package com.marianf91.market.pricecomparator.bootstrap;

import com.marianf91.market.csv.DiscountCsvRow;
import com.marianf91.market.csv.PriceCsvRow;
import com.marianf91.market.pricecomparator.model.Product;
import com.marianf91.market.pricecomparator.model.Store;
import com.marianf91.market.pricecomparator.repository.DiscountRepository;
import com.marianf91.market.pricecomparator.repository.PriceSnapshotRepository;
import com.marianf91.market.pricecomparator.repository.ProductRepository;
import com.marianf91.market.pricecomparator.repository.StoreRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
@Profile("!test")
public class CsvBootstrap implements CommandLineRunner {

    private final ProductRepository productRepo;
    private final PriceSnapshotRepository priceRepo;
    private final DiscountRepository discountRepo;
    private final StoreRepository storeRepo;
    private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    @Override
    public void run(String... args) throws Exception {
        Resource[] resources = resolver.getResources("classpath:data/*.csv");
        for (Resource res : resources) {
            String file = Objects.requireNonNull(res.getFilename());
            Store store = storeRepo.findByNameIgnoreCase(extractStore(file))
                    .orElseGet(() -> storeRepo.save(new Store(null, extractStore(file))));

            try (Reader reader = new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8)) {
                if (file.contains("discounts")) {
                    List<DiscountCsvRow> rows = new CsvToBeanBuilder<DiscountCsvRow>(reader)
                            .withType(DiscountCsvRow.class)
                            .withSeparator(';')
                            .build()
                            .parse();
                    rows.forEach(r -> {
                        String pid = r.getProductId().trim();
                        var product = productRepo.findById(pid)
                                .orElseThrow(() -> new EntityNotFoundException("Product ID=" + pid + " not found"));
                        discountRepo.save(r.toEntity(store, product));
                    });
                } else {
                    LocalDate snapshotDate = extractDate(file);
                    List<PriceCsvRow> rows = new CsvToBeanBuilder<PriceCsvRow>(reader)
                            .withType(PriceCsvRow.class)
                            .withSeparator(';')
                            .build()
                            .parse();
                    rows.forEach(r -> {
                        String pid = r.getProductId().trim();

                        Optional<Product> maybe = productRepo.findById(pid);
                        Product product = maybe.orElseGet(() -> {
                            Product p = r.toProduct();
                            p.setId(pid);
                            try {
                                return productRepo.save(p);
                            } catch (DataIntegrityViolationException ex) {
                                return productRepo.findByNameAndBrandAndQuantityAndUnit(
                                        p.getName(), p.getBrand(), p.getQuantity(), p.getUnit()
                                ).orElseThrow(() ->
                                        new EntityNotFoundException("Produs cu atributele " +
                                                p.getName() + "/" + p.getBrand() + "/" + p.getQuantity() + "/" + p.getUnit() +
                                                " nu se găsește și nu poate fi creat")
                                );
                            }
                        });

                        priceRepo.save(r.toSnapshot(store, snapshotDate, product));
                    });
                }
                log.info("Loaded data from {}", file);
            }
        }
    }

    private String extractStore(String file) {
        return file.split("_")[0];
    }

    private LocalDate extractDate(String file) {
        String part = file.split("_")[1].replace(".csv", "");
        return LocalDate.parse(part);
    }
}
