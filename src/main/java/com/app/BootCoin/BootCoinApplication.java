package com.app.BootCoin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BootCoinApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootCoinApplication.class, args);
	}

}
