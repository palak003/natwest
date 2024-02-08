package com.example.CsvProcessing;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@OpenAPIDefinition
@EnableFeignClients
public class CsvProcessingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CsvProcessingApplication.class, args);
	}

}
