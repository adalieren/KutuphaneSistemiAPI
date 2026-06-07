package com.library.management.dto;

import com.library.management.model.Student;

public class StudentDTO {
    private Long id;
    private String studentNumber;
    private String name;
    private String email;

    public StudentDTO() {
    }

    public StudentDTO(Long id, String studentNumber, String name, String email) {
        this.id = id;
        this.studentNumber = studentNumber;
        this.name = name;
        this.email = email;
    }

    public static StudentDTO fromEntity(Student student) {
        if (student == null) return null;
        return new StudentDTO(
                student.getId(),
                student.getStudentNumber(),
                student.getName(),
                student.getEmail()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
