package com.library.management.repository;

import com.library.management.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByStudentId(Long studentId);
    List<Loan> findByStudentIdAndReturnDateIsNull(Long studentId);
    List<Loan> findByBookId(Long bookId);
    List<Loan> findByBookIdAndReturnDateIsNull(Long bookId);
    Optional<Loan> findByStudentIdAndBookIdAndReturnDateIsNull(Long studentId, Long bookId);
}
