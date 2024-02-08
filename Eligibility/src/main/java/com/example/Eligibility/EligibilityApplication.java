package com.example.Eligibility;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EligibilityApplication {

	public static void main(String[] args) {
		SpringApplication.run(EligibilityApplication.class, args);
	}

}
