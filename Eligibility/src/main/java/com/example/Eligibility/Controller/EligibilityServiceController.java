package com.example.Eligibility.Controller;

import com.example.Eligibility.Entity.Eligibility;
import com.example.Eligibility.Models.EligibilityCriteria;
import com.example.Eligibility.service.EligibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EligibilityServiceController {

    @Autowired
    private EligibilityService eligibilityService;

    @PostMapping("/check-eligibility")
    public boolean checkEligibility(@RequestBody String[] studentData) {
        return eligibilityService.checkEligibility(studentData);
    }

    @PostMapping("/set-criteria")
    public void setEligibility(@RequestBody EligibilityCriteria eligibility){
        eligibilityService.setEligibility(eligibility);
    }

}