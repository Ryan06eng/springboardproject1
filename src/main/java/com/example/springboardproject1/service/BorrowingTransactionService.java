package com.example.springboardproject1.service;


import com.example.springboardproject1.model.Book;
import com.example.springboardproject1.model.Borrower;
import com.example.springboardproject1.model.BorrowingTransaction;
import com.example.springboardproject1.repository.BorrowingTransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BorrowingTransactionService {
    private static final Logger logger =
            LoggerFactory.getLogger(BorrowingTransactionService.class);

    @Value("${borrower.transaction.limit}")
    private int transactionLimit;

    private final BorrowingTransactionRepository transactionRepository;
    private final BookService bookService;
    private final BorrowerService borrowerService;

    public BorrowingTransactionService(
            BorrowingTransactionRepository transactionRepository,
            BookService bookService,
            BorrowerService borrowerService) {

        this.transactionRepository = transactionRepository;
        this.bookService = bookService;
        this.borrowerService = borrowerService;
    }

    // BORROW A BOOK
    public BorrowingTransaction borrowBook(
            Long bookId,
            Long borrowerId) {

        // Find the book
        Book book = bookService.getBookById(bookId)
                .orElseThrow(
                        () -> new RuntimeException("Book not found")
                );

        // Find the borrower
        Borrower borrower = borrowerService.getBorrowerById(borrowerId)
                .orElseThrow(
                        () -> new RuntimeException("Borrower not found")
                );
        // Count borrower's transactions
        long transactionCount =
                transactionRepository.countByBorrowerIdAndReturnDateIsNull(borrowerId);

        // Check transaction limit
        if (transactionCount >= transactionLimit) {
            throw new RuntimeException(
                    "Borrower has reached the maximum transaction limit"
            );
        }


        // Check availability
        if (!book.isAvailable()) {
            throw new RuntimeException(
                    "Book is already borrowed"
            );
        }
        logger.info("Borrowing book with ID {} by borrower with ID {}",
                bookId, borrowerId);

        // Create transaction
        BorrowingTransaction transaction =
                new BorrowingTransaction();

        transaction.setBook(book);
        transaction.setBorrower(borrower);
        transaction.setBorrowDate(LocalDate.now());
        transaction.setReturnDate(null);

        // Make book unavailable
        book.setAvailable(false);

        // Save book
        bookService.saveBook(book);

        // Save transaction
        return transactionRepository.save(transaction);
    }

    // RETURN A BOOK
    public BorrowingTransaction returnBook(Long transactionId) {

        BorrowingTransaction transaction =
                transactionRepository.findById(transactionId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Transaction not found"
                                )
                        );

        // Check if already returned
        if (transaction.getReturnDate() != null) {
            throw new RuntimeException(
                    "Book has already been returned"
            );
        }
        logger.info("Returning borrowing transaction with ID {}",
                transactionId);

        // Set return date
        transaction.setReturnDate(LocalDate.now());

        // Make book available again
        Book book = transaction.getBook();
        book.setAvailable(true);

        bookService.saveBook(book);

        return transactionRepository.save(transaction);
    }

    // GET ALL TRANSACTIONS
    public List<BorrowingTransaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // GET ONE TRANSACTION
    public BorrowingTransaction getTransactionById(Long id) {

        return transactionRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Transaction not found"
                        )
                );
    }
}
