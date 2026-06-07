package com.library.management.controller;

import com.library.management.dto.BookDTO;
import com.library.management.dto.LoanDTO;
import com.library.management.model.Book;
import com.library.management.service.BookService;
import com.library.management.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final LoanService loanService;

    @Autowired
    public BookController(BookService bookService, LoanService loanService) {
        this.bookService = bookService;
        this.loanService = loanService;
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> dtos = bookService.getAllBooks()
                .stream()
                .map(BookDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(BookDTO.fromEntity(book));
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody Book book) {
        Book createdBook = bookService.createBook(book);
        return new ResponseEntity<>(BookDTO.fromEntity(createdBook), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody Book bookDetails) {
        Book updatedBook = bookService.updateBook(id, bookDetails);
        return ResponseEntity.ok(BookDTO.fromEntity(updatedBook));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<LoanDTO>> getBookBorrowers(@PathVariable Long id) {
        List<LoanDTO> dtos = loanService.getLoansByBookId(id)
                .stream()
                .map(LoanDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }
}
