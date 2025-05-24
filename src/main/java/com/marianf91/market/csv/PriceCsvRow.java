package com.marianf91.market.csv;

import com.marianf91.market.pricecomparator.model.PriceSnapshot;
import com.marianf91.market.pricecomparator.model.Product;
import com.marianf91.market.pricecomparator.model.Store;
import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PriceCsvRow {
    @CsvBindByName(column = "product_id")
    private String productId;
    @CsvBindByName(column = "product_name")
    private String productName;
    @CsvBindByName(column = "product_category")
    private String category;
    @CsvBindByName(column = "brand")
    private String brand;
    @CsvBindByName(column = "package_quantity")
    private double quantity;
    @CsvBindByName(column = "package_unit")
    private String unit;
    @CsvBindByName(column = "price")
    private BigDecimal price;
    @CsvBindByName(column = "currency")
    private String currency;

    public Product toProduct() {
        return Product.builder()
                .id(productId)
                .name(productName)
                .category(category)
                .brand(brand)
                .quantity(quantity)
                .unit(unit)
                .build();
    }

    public PriceSnapshot toSnapshot(Store store, LocalDate date, Product product) {
        return PriceSnapshot.builder()
                .product(product)
                .store(store)
                .date(date)
                .price(price)
                .currency(currency)
                .build();
    }
}