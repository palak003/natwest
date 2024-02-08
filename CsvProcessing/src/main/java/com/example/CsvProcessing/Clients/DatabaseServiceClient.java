package com.example.CsvProcessing.Clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "DATABASE-SERVICE", url = "http://localhost:8081")
public interface DatabaseServiceClient {

    @PostMapping("/students")
    void saveOrUpdateStudent(@RequestParam("rollNumber") Long rollNumber, @RequestParam("isEligible") boolean isEligible);

    @GetMapping("/check/{rollNumber}")
    String checkEligibility(@PathVariable Long rollNumber);
}
