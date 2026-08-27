package com.example.springboardproject1.dto;

import java.time.LocalDate;

public record BorrowingTransactionResponse(
        Long id,
        Long bookId,
        String bookTitle,
        Long borrowerId,
        String borrowerName,
        LocalDate borrowDate,
        LocalDate returnDate
) {
}