package com.example.DatabaseService;

import com.example.DatabaseService.Entity.Student;
import com.example.DatabaseService.Repository.StudentRepository;
import com.example.DatabaseService.Service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;

@SpringBootTest
public class StudentServiceTest {

    @MockBean
    private StudentRepository studentRepository;

    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        studentService = new StudentService(studentRepository);
    }

    @Test
    public void checkEligibilityTest() {
        Student mockStudent = new Student();
        mockStudent.setEligible(true);
        mockStudent.setRollNumber(1L);

        when(studentRepository.findByRollNumber(1L)).thenReturn(mockStudent);

        String result = studentService.checkEligibility(1L);
        Assertions.assertEquals("Eligible", result);
    }

    @Test
    public void saveOrUpdateStudentTest() {

        List<Student> studentList = new ArrayList<>();
        Student mockStudent = new Student();
        mockStudent.setEligible(true);
        studentList.add(mockStudent);

        studentService.saveOrUpdateStudent(studentList);
        verify(studentRepository, times(1)).saveAll(studentList);
    }
}