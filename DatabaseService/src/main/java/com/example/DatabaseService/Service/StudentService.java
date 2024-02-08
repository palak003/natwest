package com.example.DatabaseService.Service;

import com.example.DatabaseService.Entity.Student;
import com.example.DatabaseService.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public void saveOrUpdateStudent(Long rollNumber, boolean isEligible) {
        Student existingStudent = studentRepository.findByRollNumber(rollNumber);
        if (existingStudent == null) {
            Student newStudent = Student.builder()
                    .rollNumber(rollNumber)
                    .eligible(isEligible)
                    .build();
            studentRepository.save(newStudent);
        } else {
            existingStudent.setEligible(isEligible);
            studentRepository.save(existingStudent);
        }
    }

    public String checkEligibility(Long rollNumber) {
        Student student = studentRepository.findByRollNumber(rollNumber);
        if (student != null) {
            return student.isEligible() ? "Eligible" : "Not Eligible";
        } else {
            return "Not Applicable";
        }
    }
}
