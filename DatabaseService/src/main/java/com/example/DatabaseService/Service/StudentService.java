package com.example.DatabaseService.Service;

import com.example.DatabaseService.Entity.Student;
import com.example.DatabaseService.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    
    

    public String checkEligibility(Long rollNumber) {
        Student student = studentRepository.findByRollNumber(rollNumber);
        if (student != null) {
            return student.isEligible() ? "Eligible" : "Not Eligible";
        } else {
            return "Not Applicable";
        }
    }

    public void saveOrUpdateStudent(List<Student> students) {
        studentRepository.saveAll(students);
    }


}
