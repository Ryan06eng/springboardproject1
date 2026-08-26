package com.example.springboardproject1.repository;

import com.example.springboardproject1.model.BorrowingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BorrowingTransactionRepository
        extends JpaRepository<BorrowingTransaction, Long> {

    Optional<BorrowingTransaction> findByBookIdAndReturnDateIsNull(
            Long bookId
    );

    long countByBorrowerIdAndReturnDateIsNull(Long borrowerId);
}
