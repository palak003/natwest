package com.example.CsvProcessing.Clients;

import com.example.CsvProcessing.Models.Student;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = "DATABASE-SERVICE", url = "http://localhost:8081")
public interface DatabaseServiceClient {


    @GetMapping("/check/{rollNumber}")
    String checkEligibility(@PathVariable Long rollNumber);


    @PostMapping("/students")
    void saveOrUpdateStudents(List<Student> students);
}
