package com.library.management.repository;

import com.library.management.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentNumber(String studentNumber);
    Optional<Student> findByEmail(String email);
    boolean existsByStudentNumber(String studentNumber);
    boolean existsByEmail(String email);
}
