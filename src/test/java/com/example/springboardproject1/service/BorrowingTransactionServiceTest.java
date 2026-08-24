package com.example.springboardproject1.service;

import com.example.springboardproject1.model.Book;
import com.example.springboardproject1.model.Borrower;
import com.example.springboardproject1.model.BorrowingTransaction;
import com.example.springboardproject1.repository.BookRepository;
import com.example.springboardproject1.repository.BorrowerRepository;
import com.example.springboardproject1.repository.BorrowingTransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


//import com.example.springbootproject1.service.BookService

@ExtendWith(MockitoExtension.class)
class BorrowingTransactionServiceTest {

    @Mock
    private BorrowingTransactionRepository transactionRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BorrowerRepository borrowerRepository;
    @Mock
    private BookService bookService;

    @InjectMocks
    private BorrowingTransactionService transactionService;

    @Test
    void shouldBorrowAvailableBook() {

        Book book = new Book();
        book.setId(1L);
        book.setAvailable(true);

        Borrower borrower = new Borrower();
        borrower.setId(1L);



        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));
        //when(bookService.getBookById(1L))
                //.thenReturn(Optional.of(book));

        when(borrowerRepository.findById(1L))
                .thenReturn(Optional.of(borrower));

        when(transactionRepository.save(any(BorrowingTransaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BorrowingTransaction transaction =
                transactionService.borrowBook(1L, 1L);

        assertNotNull(transaction);
        assertEquals(book, transaction.getBook());
        assertEquals(borrower, transaction.getBorrower());
        assertFalse(book.isAvailable());

        verify(bookRepository).save(book);
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

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));
        // when(bookService.getBookById(1L))
               // .thenReturn(Optional.of(book));

        when(borrowerRepository.findById(1L))
                .thenReturn(Optional.of(borrower));

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
}