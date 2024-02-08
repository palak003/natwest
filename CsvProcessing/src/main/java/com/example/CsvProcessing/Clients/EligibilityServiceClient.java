package com.example.CsvProcessing.Clients;

import com.example.CsvProcessing.Models.EligibilityCriteria;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "ELIGIBILITY-SERVICE", url = "http://localhost:8082")
public interface EligibilityServiceClient {

    @PostMapping("/check-eligibility")
    boolean checkEligibility(@RequestBody String[] studentData);

    @PostMapping("/set-criteria")
    void setEligibility(@RequestBody EligibilityCriteria eligibility);
}
