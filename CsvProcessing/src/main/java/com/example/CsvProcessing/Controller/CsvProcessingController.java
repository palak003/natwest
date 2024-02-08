package com.example.CsvProcessing.Controller;


import com.example.CsvProcessing.Models.EligibilityCriteria;
import com.example.CsvProcessing.Service.CsvProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class CsvProcessingController {

    @Autowired
    private CsvProcessingService csvProcessingService;

    @PostMapping("/process-csv")
    public ResponseEntity<byte[]> processMultipleCsv(@RequestParam("files") List<MultipartFile> files) {
        byte[] zipBytes = csvProcessingService.processMultipleCsvFiles(files);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "updated_files.zip");

        return ResponseEntity.ok()
                .headers(headers)
                .body(zipBytes);
    }

    @GetMapping("/check-eligibility/{rollNumber}")
    public String checkEligibility(@PathVariable Long rollNumber) {
        return csvProcessingService.checkEligibility(rollNumber);
    }

    @PostMapping("/set-eligibility")
    public void setEligibility(@RequestBody EligibilityCriteria object) {
        csvProcessingService.setEligibility(object);
    }
}