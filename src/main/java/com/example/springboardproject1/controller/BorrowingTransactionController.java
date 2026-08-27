package com.example.springboardproject1.controller;

import com.example.springboardproject1.dto.BorrowingTransactionResponse;
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

    private BorrowingTransactionResponse toResponse(
            BorrowingTransaction transaction) {

        return new BorrowingTransactionResponse(
                transaction.getId(),
                transaction.getBook().getId(),
                transaction.getBook().getTitle(),
                transaction.getBorrower().getId(),
                transaction.getBorrower().getName(),
                transaction.getBorrowDate(),
                transaction.getReturnDate()
        );
    }

    // BORROW BOOK
    @PostMapping("/borrow")
    public ResponseEntity<BorrowingTransactionResponse> borrowBook(
            @RequestParam Long bookId,
            @RequestParam Long borrowerId) {

        BorrowingTransaction transaction =
                transactionService.borrowBook(bookId, borrowerId);

        return ResponseEntity.ok(toResponse(transaction));
    }

    // RETURN BOOK
    @PostMapping("/return/{transactionId}")
    public ResponseEntity<BorrowingTransactionResponse> returnBook(
            @PathVariable Long transactionId) {

        BorrowingTransaction transaction =
                transactionService.returnBook(transactionId);

        return ResponseEntity.ok(toResponse(transaction));
    }

    // GET ALL TRANSACTIONS
    @GetMapping
    public List<BorrowingTransactionResponse> getAllTransactions() {
        return transactionService.getAllTransactions()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // GET ONE TRANSACTION
    @GetMapping("/{id}")
    public ResponseEntity<BorrowingTransactionResponse> getTransaction(
            @PathVariable Long id) {

        BorrowingTransaction transaction =
                transactionService.getTransactionById(id);

        return ResponseEntity.ok(toResponse(transaction));
    }
}