package com.example.Eligibility;

import com.example.Eligibility.Models.EligibilityCriteria;
import com.example.Eligibility.service.EligibilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class EligibilityServiceTest {

    private EligibilityService eligibilityService;
    private CacheManager cacheManager;
    private Cache cache;

    @BeforeEach
    public void setup() {
        cacheManager = Mockito.mock(CacheManager.class);
        cache = Mockito.mock(Cache.class);
        when(cacheManager.getCache("defaultCriteria")).thenReturn(cache);
        eligibilityService = new EligibilityService(cacheManager);
    }

    @Test
    public void testCheckEligibility() {
        EligibilityCriteria criteria = EligibilityCriteria.builder()
                .scienceMarks(85)
                .mathsMarks(90)
                .computerMarks(60)
                .englishMarks(75)
                .build();
        when(cache.get("default", EligibilityCriteria.class)).thenReturn(criteria);

        String[] studentDataQualified = new String[]{"", "", "90", "91", "80", "70"};
        assertTrue(eligibilityService.checkEligibility(studentDataQualified));

        String[] studentDataNotQualified = new String[]{"", "", "80", "80", "70", "60"};
        assertFalse(eligibilityService.checkEligibility(studentDataNotQualified));
    }
}