package com.example.DatabaseService.Repository;

import com.example.DatabaseService.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByRollNumber(Long rollNumber);
}