package com.bankinc.card_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.bankinc")
public class CardSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardSystemApplication.class, args);
	}

}
