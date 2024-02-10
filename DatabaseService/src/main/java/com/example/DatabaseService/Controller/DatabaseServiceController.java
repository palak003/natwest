package com.example.DatabaseService.Controller;

import com.example.DatabaseService.Entity.Student;
import com.example.DatabaseService.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class DatabaseServiceController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/check/{rollNumber}")
    public String checkEligibility(@PathVariable Long rollNumber){
        return studentService.checkEligibility(rollNumber);
    }

    @PostMapping("/students")
    void saveOrUpdateStudents(@RequestBody List<Student> students){
        studentService.saveOrUpdateStudent(students);
    }
}