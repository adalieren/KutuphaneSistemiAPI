package com.library.management.service;

import com.library.management.exception.BusinessException;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.model.Book;
import com.library.management.model.Loan;
import com.library.management.repository.BookRepository;
import com.library.management.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    @Autowired
    public BookService(BookRepository bookRepository, LoanRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kitap bulunamadı. ID: " + id));
    }

    @Transactional
    public Book createBook(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new BusinessException("Bu ISBN numarasına sahip kitap zaten kayıtlı: " + book.getIsbn());
        }
        book.setAvailable(true);
        return bookRepository.save(book);
    }

    @Transactional
    public Book updateBook(Long id, Book bookDetails) {
        Book book = getBookById(id);

        bookRepository.findByIsbn(bookDetails.getIsbn())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new BusinessException("Bu ISBN numarası başka bir kitaba ait: " + bookDetails.getIsbn());
                    }
                });

        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setIsbn(bookDetails.getIsbn());

        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = getBookById(id);

        List<Loan> activeLoans = loanRepository.findByBookIdAndReturnDateIsNull(id);
        if (!activeLoans.isEmpty()) {
            throw new BusinessException("Kitap silinemez çünkü şu anda bir öğrenci tarafından ödünç alınmış durumda.");
        }

        bookRepository.delete(book);
    }
}
