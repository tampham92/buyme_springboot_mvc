package com.tampham;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WebBuymeSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebBuymeSpringApplication.class, args);
	}

}
