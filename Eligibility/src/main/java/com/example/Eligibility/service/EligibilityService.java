package com.example.Eligibility.service;


import com.example.Eligibility.Models.EligibilityCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class EligibilityService {

    private final Cache defaultCriteriaCache;

    @Autowired
    public EligibilityService(CacheManager cacheManager) {
        this.defaultCriteriaCache = cacheManager.getCache("defaultCriteria");
        if (defaultCriteriaCache.get("default") == null) {
            defaultCriteriaCache.put("default", getDefaultEligibilityCriteria());
        }
    }

    @Cacheable(value = "defaultCriteria", key = "'default'")
    private EligibilityCriteria getDefaultEligibilityCriteria() {
        return EligibilityCriteria.builder()
                .scienceMarks(85)
                .mathsMarks(90)
                .computerMarks(60)
                .englishMarks(75)
                .build();
    }

    public boolean checkEligibility(String[] studentData) {
        int scienceMarks = Integer.parseInt(studentData[2]);
        int mathsMarks = Integer.parseInt(studentData[3]);
        int englishMarks = Integer.parseInt(studentData[4]);
        int computerMarks = Integer.parseInt(studentData[5]);

        EligibilityCriteria existingCriteria = defaultCriteriaCache.get("default", EligibilityCriteria.class);

        return (scienceMarks > existingCriteria.getScienceMarks() &&
                mathsMarks > existingCriteria.getMathsMarks() &&
                computerMarks > existingCriteria.getComputerMarks() &&
                englishMarks > existingCriteria.getEnglishMarks());
    }

    public void setEligibility(EligibilityCriteria criteria) {
        defaultCriteriaCache.put("default", criteria);
    }

}
