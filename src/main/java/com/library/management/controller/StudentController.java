package com.library.management.controller;

import com.library.management.dto.LoanDTO;
import com.library.management.dto.StudentDTO;
import com.library.management.model.Student;
import com.library.management.service.LoanService;
import com.library.management.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final LoanService loanService;

    @Autowired
    public StudentController(StudentService studentService, LoanService loanService) {
        this.studentService = studentService;
        this.loanService = loanService;
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> dtos = studentService.getAllStudents()
                .stream()
                .map(StudentDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(StudentDTO.fromEntity(student));
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody Student student) {
        Student createdStudent = studentService.createStudent(student);
        return new ResponseEntity<>(StudentDTO.fromEntity(createdStudent), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @Valid @RequestBody Student studentDetails) {
        Student updatedStudent = studentService.updateStudent(id, studentDetails);
        return ResponseEntity.ok(StudentDTO.fromEntity(updatedStudent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<LoanDTO>> getBorrowedBooks(@PathVariable Long id) {
        List<LoanDTO> dtos = loanService.getLoansByStudentId(id)
                .stream()
                .map(LoanDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }
}
