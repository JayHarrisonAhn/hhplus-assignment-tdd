package com.example.concert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ConcertApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConcertApplication.class, args);
	}

}
