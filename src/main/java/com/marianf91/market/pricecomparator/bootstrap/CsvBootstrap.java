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
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        List<Resource> priceFiles = new ArrayList<>();
        List<Resource> discountFiles = new ArrayList<>();

        for (Resource res : resources) {
            String filename = res.getFilename();
            if (filename != null && filename.contains("discounts")) {
                discountFiles.add(res);
            } else {
                priceFiles.add(res);
            }
        }

        processPriceFiles(priceFiles);
        processDiscountFiles(discountFiles);
    }

    private void processPriceFiles(List<Resource> files) {
        for (Resource res : files) {
            String file = res.getFilename();
            Store store = findOrCreateStore(extractStore(file));
            LocalDate date = extractDate(file);
            try (Reader reader = new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8)) {
                List<PriceCsvRow> rows = new CsvToBeanBuilder<PriceCsvRow>(reader)
                        .withType(PriceCsvRow.class)
                        .withSeparator(';')
                        .build()
                        .parse();

                for (PriceCsvRow r : rows) {
                    String pid = r.getProductId().trim();
                    Product product = productRepo.findById(pid)
                            .orElseGet(() -> {
                                Product np = r.toProduct();
                                np.setId(pid);
                                return productRepo.save(np);
                            });

                    priceRepo.save(r.toSnapshot(store, date, product));
                }
            } catch (Exception e) {
                log.error("Failed to load prices from {}", file, e);
            }
            log.info("Loaded prices from {}", file);
        }
    }

    private void processDiscountFiles(List<Resource> files) {
        for (Resource res : files) {
            String file = res.getFilename();
            Store store = findOrCreateStore(extractStore(file));
            try (Reader reader = new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8)) {
                List<DiscountCsvRow> rows = new CsvToBeanBuilder<DiscountCsvRow>(reader)
                        .withType(DiscountCsvRow.class)
                        .withSeparator(';')
                        .build()
                        .parse();

                for (DiscountCsvRow r : rows) {
                    String pid = r.getProductId().trim();
                    Product product = productRepo.findById(pid)
                            .orElseThrow(() -> new EntityNotFoundException("Product ID=" + pid + " not found"));
                    discountRepo.save(r.toEntity(store, product));
                }
            } catch (Exception e) {
                log.error("Failed to load discounts from {}", file, e);
            }
            log.info("Loaded discounts from {}", file);
        }
    }

    private Store findOrCreateStore(String name) {
        return storeRepo.findByNameIgnoreCase(name)
                .orElseGet(() -> storeRepo.save(new Store(null, name)));
    }

    private String extractStore(String filename) {
        return filename.split("_")[0];
    }

    private LocalDate extractDate(String filename) {
        String part = filename.split("_")[1].replaceAll("\\.csv$", "");
        return LocalDate.parse(part);
    }
}