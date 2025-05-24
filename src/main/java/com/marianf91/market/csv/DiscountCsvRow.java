package com.marianf91.market.csv;

import com.marianf91.market.pricecomparator.model.Discount;
import com.marianf91.market.pricecomparator.model.Product;
import com.marianf91.market.pricecomparator.model.Store;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DiscountCsvRow {

    @CsvBindByName(column = "product_id")
    private String productId;

    @CsvCustomBindByName(column = "from_date", converter = LocalDateConverter.class)
    private LocalDate fromDate;

    @CsvCustomBindByName(column = "to_date", converter = LocalDateConverter.class)
    private LocalDate toDate;

    @CsvBindByName(column = "percentage_of_discount")
    private int percentage;

    public Discount toEntity(Store store, Product product) {
        return Discount.builder()
                .product(product)
                .store(store)
                .fromDate(fromDate)
                .toDate(toDate)
                .percentage(percentage)
                .build();
    }
}