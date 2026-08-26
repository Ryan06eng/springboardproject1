package com.example.springboardproject1.service;

import com.example.springboardproject1.model.Book;
import com.example.springboardproject1.model.Borrower;
import com.example.springboardproject1.model.BorrowingTransaction;
import com.example.springboardproject1.repository.BorrowingTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowingTransactionServiceTest {

    @Mock
    private BorrowingTransactionRepository transactionRepository;

    @Mock
    private BookService bookService;

    @Mock
    private BorrowerService borrowerService;

    @InjectMocks
    private BorrowingTransactionService transactionService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(
                transactionService,
                "transactionLimit",
                5
        );
    }

    @Test
    void shouldBorrowAvailableBook() {

        Book book = new Book();
        book.setId(1L);
        book.setAvailable(true);

        Borrower borrower = new Borrower();
        borrower.setId(1L);

        when(bookService.getBookById(1L))
                .thenReturn(Optional.of(book));

        when(borrowerService.getBorrowerById(1L))
                .thenReturn(Optional.of(borrower));

        when(transactionRepository
                .countByBorrowerIdAndReturnDateIsNull(1L))
                .thenReturn(0L);

        when(transactionRepository.save(any(BorrowingTransaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BorrowingTransaction transaction =
                transactionService.borrowBook(1L, 1L);

        assertNotNull(transaction);
        assertEquals(book, transaction.getBook());
        assertEquals(borrower, transaction.getBorrower());
        assertFalse(book.isAvailable());

        verify(bookService).saveBook(book);

        verify(transactionRepository)
                .save(any(BorrowingTransaction.class));
    }

    @Test
    void shouldNotBorrowUnavailableBook() {

        Book book = new Book();
        book.setId(1L);
        book.setAvailable(false);

        Borrower borrower = new Borrower();
        borrower.setId(1L);

        when(bookService.getBookById(1L))
                .thenReturn(Optional.of(book));

        when(borrowerService.getBorrowerById(1L))
                .thenReturn(Optional.of(borrower));

        when(transactionRepository
                .countByBorrowerIdAndReturnDateIsNull(1L))
                .thenReturn(0L);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> transactionService.borrowBook(1L, 1L)
        );

        assertEquals(
                "Book is already borrowed",
                exception.getMessage()
        );

        verify(transactionRepository, never())
                .save(any(BorrowingTransaction.class));
    }

    @Test
    void shouldNotBorrowWhenTransactionLimitIsReached() {

        Book book = new Book();
        book.setId(1L);
        book.setAvailable(true);

        Borrower borrower = new Borrower();
        borrower.setId(1L);

        when(bookService.getBookById(1L))
                .thenReturn(Optional.of(book));

        when(borrowerService.getBorrowerById(1L))
                .thenReturn(Optional.of(borrower));

        when(transactionRepository
                .countByBorrowerIdAndReturnDateIsNull(1L))
                .thenReturn(5L);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> transactionService.borrowBook(1L, 1L)
        );

        assertEquals(
                "Borrower has reached the maximum transaction limit",
                exception.getMessage()
        );

        verify(transactionRepository, never())
                .save(any(BorrowingTransaction.class));
    }
}