package com.library.management.service;

import com.library.management.exception.BusinessException;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.model.Book;
import com.library.management.model.Loan;
import com.library.management.model.Student;
import com.library.management.repository.BookRepository;
import com.library.management.repository.LoanRepository;
import com.library.management.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository, StudentRepository studentRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.studentRepository = studentRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Loan borrowBook(Long studentId, Long bookId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Öğrenci bulunamadı. ID: " + studentId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Kitap bulunamadı. ID: " + bookId));

        if (!book.isAvailable()) {
            throw new BusinessException("Kitap şu anda kütüphanede mevcut değil (başka bir öğrenci tarafından ödünç alınmış).");
        }

        book.setAvailable(false);
        bookRepository.save(book);

        Loan loan = new Loan(student, book, LocalDate.now(), LocalDate.now().plusDays(14));
        return loanRepository.save(loan);
    }

    @Transactional
    public Loan returnBook(Long studentId, Long bookId) {
        Loan loan = loanRepository.findByStudentIdAndBookIdAndReturnDateIsNull(studentId, bookId)
                .orElseThrow(() -> new BusinessException("Bu öğrenci ve kitap için aktif bir ödünç kaydı bulunamadı."));

        loan.setReturnDate(LocalDate.now());
        
        Book book = loan.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    @Transactional(readOnly = true)
    public List<Loan> getLoansByStudentId(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Öğrenci bulunamadı. ID: " + studentId);
        }
        return loanRepository.findByStudentId(studentId);
    }

    @Transactional(readOnly = true)
    public List<Loan> getLoansByBookId(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("Kitap bulunamadı. ID: " + bookId);
        }
        return loanRepository.findByBookId(bookId);
    }
}
