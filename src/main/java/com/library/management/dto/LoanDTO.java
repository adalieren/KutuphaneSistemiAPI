package com.library.management.dto;

import com.library.management.model.Loan;
import java.time.LocalDate;

public class LoanDTO {
    private Long id;
    private Long studentId;
    private String studentName;
    private String studentNumber;
    private Long bookId;
    private String bookTitle;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public LoanDTO() {
    }

    public LoanDTO(Long id, Long studentId, String studentName, String studentNumber,
                   Long bookId, String bookTitle, LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentNumber = studentNumber;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public static LoanDTO fromEntity(Loan loan) {
        if (loan == null) return null;
        return new LoanDTO(
                loan.getId(),
                loan.getStudent() != null ? loan.getStudent().getId() : null,
                loan.getStudent() != null ? loan.getStudent().getName() : null,
                loan.getStudent() != null ? loan.getStudent().getStudentNumber() : null,
                loan.getBook() != null ? loan.getBook().getId() : null,
                loan.getBook() != null ? loan.getBook().getTitle() : null,
                loan.getBorrowDate(),
                loan.getDueDate(),
                loan.getReturnDate()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
