package com.example.Eligibility.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EligibilityCriteria {
    private int scienceMarks;
    private int mathsMarks;
    private int computerMarks;
    private int englishMarks;
}
