package io.endeavour.stocks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
public class StockAnalyticsApplication {

	public static void main(String[] args) {

		SpringApplication.run(StockAnalyticsApplication.class, args);
	}

}
