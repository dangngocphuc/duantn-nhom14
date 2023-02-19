package com.example.DaPhone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DaPhoneApplication {
	public static void main(String[] args) {
		SpringApplication.run(DaPhoneApplication.class, args);
	}
}
