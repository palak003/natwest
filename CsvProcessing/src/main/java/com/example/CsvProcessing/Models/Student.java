package com.example.CsvProcessing.Models;

public class Student {

   private Boolean eligible;
   private Long rollNumber;

    public Boolean getEligible() {
        return eligible;
    }

    public void setEligible(Boolean eligible) {
       this.eligible = eligible;
    }

    public Long getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(Long rollNumber) {
        this.rollNumber = rollNumber;
    }
}
