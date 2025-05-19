package com.marianf91.market.pricecomparator;

import org.springframework.boot.SpringApplication;

public class TestPriceComparatorApplication {

	public static void main(String[] args) {
		SpringApplication.from(PriceComparatorApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
