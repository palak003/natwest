package com.example.DatabaseService.Controller;

import com.example.DatabaseService.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DatabaseServiceController {

    @Autowired
    private StudentService studentService;
    @PostMapping("/students")
    public void saveOrUpdateStudent(@RequestParam("rollNumber") Long rollNumber, @RequestParam("isEligible") boolean isEligible) {
        studentService.saveOrUpdateStudent(rollNumber, isEligible);
    }

    @GetMapping("/check/{rollNumber}")
    public String checkEligibility(@PathVariable Long rollNumber){
        return studentService.checkEligibility(rollNumber);
    }
}