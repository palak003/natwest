package com.example.Eligibility.service;

import com.example.Eligibility.Entity.Eligibility;
import com.example.Eligibility.Models.EligibilityCriteria;
import com.example.Eligibility.Repository.EligibilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EligibilityService {

    @Autowired
    private EligibilityRepository eligibilityRepository;

    public boolean checkEligibility(String[] studentData) {
        int scienceMarks = Integer.parseInt(studentData[2]);
        int mathsMarks = Integer.parseInt(studentData[3]);
        int englishMarks = Integer.parseInt(studentData[4]);
        int computerMarks = Integer.parseInt(studentData[5]);
        Eligibility existingCriteria= getDefaultEligibilityCriteria();
        var existingCriterias=eligibilityRepository.findAll();
        if(existingCriterias.size()>0)
           existingCriteria=existingCriterias.get(0);
        return (scienceMarks > existingCriteria.getScienceMarks() &&
                mathsMarks > existingCriteria.getMathsMarks() &&
                computerMarks > existingCriteria.getComputerMarks() &&
                englishMarks > existingCriteria.getEnglishMarks());
    }

    public void setEligibility(EligibilityCriteria criteria) {
        Eligibility newCriteria=convertToEligibility(criteria);
        Eligibility existingCriteria = eligibilityRepository.findAll().stream().findFirst().orElse(null);
        if (existingCriteria == null) {
            existingCriteria = newCriteria;
        } else {
            existingCriteria = updateExistingCriteria(existingCriteria,newCriteria);
        }
        eligibilityRepository.save(existingCriteria);
    }

    private Eligibility getDefaultEligibilityCriteria() {
        return Eligibility.builder()
                .scienceMarks(85)
                .mathsMarks(90)
                .computerMarks(60)
                .englishMarks(75)
                .build();
    }

    private Eligibility updateExistingCriteria(Eligibility existingCriteria, Eligibility newCriteria) {
        existingCriteria.setScienceMarks(newCriteria.getScienceMarks());
        existingCriteria.setMathsMarks(newCriteria.getMathsMarks());
        existingCriteria.setComputerMarks(newCriteria.getComputerMarks());
        existingCriteria.setEnglishMarks(newCriteria.getEnglishMarks());
        return existingCriteria;
    }

    private static Eligibility convertToEligibility(EligibilityCriteria criteria) {
        Eligibility eligibility = new Eligibility();
        eligibility.setScienceMarks(criteria.getScienceMarks());
        eligibility.setMathsMarks(criteria.getMathsMarks());
        eligibility.setComputerMarks(criteria.getComputerMarks());
        eligibility.setEnglishMarks(criteria.getEnglishMarks());
        return eligibility;
    }
}
