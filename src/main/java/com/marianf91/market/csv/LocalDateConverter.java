package com.marianf91.market.csv;

import com.opencsv.bean.AbstractBeanField;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter extends AbstractBeanField<LocalDate, String> {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd

    @Override
    protected LocalDate convert(String value) {
        return LocalDate.parse(value.trim(), FMT);
    }
}