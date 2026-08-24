package com.example.springboardproject1.controller;

import com.example.springboardproject1.model.BorrowingTransaction;
import com.example.springboardproject1.service.BorrowingTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class BorrowingTransactionController {

    private final BorrowingTransactionService transactionService;

    public BorrowingTransactionController(
            BorrowingTransactionService transactionService) {

        this.transactionService = transactionService;
    }

    // BORROW BOOK
    @PostMapping("/borrow")
    public ResponseEntity<?> borrowBook(
            @RequestParam Long bookId,
            @RequestParam Long borrowerId) {

        try {

            return ResponseEntity.ok(
                    transactionService.borrowBook(
                            bookId,
                            borrowerId
                    )
            );

        } catch (RuntimeException e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    // RETURN BOOK
    @PostMapping("/return/{transactionId}")
    public ResponseEntity<?> returnBook(
            @PathVariable Long transactionId) {

        try {

            return ResponseEntity.ok(
                    transactionService.returnBook(
                            transactionId
                    )
            );

        } catch (RuntimeException e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    // get all transactions
    @GetMapping
    public List<BorrowingTransaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    // GET ONE TRANSACTION
    @GetMapping("/{id}")
    public ResponseEntity<BorrowingTransaction> getTransaction(
            @PathVariable Long id) {

        try {

            return ResponseEntity.ok(
                    transactionService.getTransactionById(id)
            );

        } catch (RuntimeException e) {

            return ResponseEntity.notFound().build();
        }
    }
}
