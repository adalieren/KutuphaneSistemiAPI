package com.library.management.controller;

import com.library.management.dto.LoanDTO;
import com.library.management.model.Loan;
import com.library.management.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/borrow")
    public ResponseEntity<LoanDTO> borrowBook(@RequestParam Long studentId, @RequestParam Long bookId) {
        Loan loan = loanService.borrowBook(studentId, bookId);
        return new ResponseEntity<>(LoanDTO.fromEntity(loan), HttpStatus.CREATED);
    }

    @PostMapping("/return")
    public ResponseEntity<LoanDTO> returnBook(@RequestParam Long studentId, @RequestParam Long bookId) {
        Loan loan = loanService.returnBook(studentId, bookId);
        return ResponseEntity.ok(LoanDTO.fromEntity(loan));
    }
}
