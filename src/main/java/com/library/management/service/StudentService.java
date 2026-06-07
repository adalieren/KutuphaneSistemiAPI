package com.library.management.service;

import com.library.management.exception.BusinessException;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.model.Loan;
import com.library.management.model.Student;
import com.library.management.model.Role;
import com.library.management.model.User;
import com.library.management.repository.LoanRepository;
import com.library.management.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final LoanRepository loanRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StudentService(StudentRepository studentRepository, LoanRepository loanRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.loanRepository = loanRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Öğrenci bulunamadı. ID: " + id));
    }

    @Transactional
    public Student createStudent(Student student) {
        if (studentRepository.existsByStudentNumber(student.getStudentNumber())) {
            throw new BusinessException("Bu öğrenci numarası zaten kayıtlı: " + student.getStudentNumber());
        }
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new BusinessException("Bu e-posta adresi zaten kayıtlı: " + student.getEmail());
        }

        // Create associated user login
        User user = new User();
        user.setUsername(student.getStudentNumber());
        user.setPassword(passwordEncoder.encode(student.getStudentNumber())); // default password is studentNumber
        user.setRole(Role.STUDENT);
        student.setUser(user);

        return studentRepository.save(student);
    }

    @Transactional
    public Student updateStudent(Long id, Student studentDetails) {
        Student student = getStudentById(id);

        studentRepository.findByStudentNumber(studentDetails.getStudentNumber())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new BusinessException("Bu öğrenci numarası başka bir öğrenciye ait: " + studentDetails.getStudentNumber());
                    }
                });

        studentRepository.findByEmail(studentDetails.getEmail())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new BusinessException("Bu e-posta adresi başka bir öğrenciye ait: " + studentDetails.getEmail());
                    }
                });

        student.setName(studentDetails.getName());
        student.setStudentNumber(studentDetails.getStudentNumber());
        student.setEmail(studentDetails.getEmail());

        if (student.getUser() != null) {
            student.getUser().setUsername(studentDetails.getStudentNumber());
            student.getUser().setPassword(passwordEncoder.encode(studentDetails.getStudentNumber()));
        }

        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = getStudentById(id);

        List<Loan> activeLoans = loanRepository.findByStudentIdAndReturnDateIsNull(id);
        if (!activeLoans.isEmpty()) {
            throw new BusinessException("Öğrenci silinemez çünkü teslim etmediği kitaplar bulunmaktadır.");
        }

        studentRepository.delete(student);
    }
}
