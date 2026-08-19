package com.example.springboardproject1.service;

import com.example.springboardproject1.model.Book;
import com.example.springboardproject1.model.BookCategory;
import com.example.springboardproject1.repository.BookRepository;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Create
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    // read - all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Read - one book
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    // update
    public Book updateBook(Long id, Book bookDetails) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setTitle(bookDetails.getTitle());
        book.setIsbn(bookDetails.getIsbn());
        book.setCategory(bookDetails.getCategory());
        book.setAuthor(bookDetails.getAuthor());
        book.setAvailable(bookDetails.isAvailable());

        return bookRepository.save(book);
    }
    // Delete.
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    // search by title.
    public List<Book> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    // Search by category.
    public List<Book> searchByCategory(BookCategory category) {
        return bookRepository.findByCategory(category);
    }

    // search by author.
    public List<Book> searchByAuthor(String authorName) {
        return bookRepository.findByAuthorNameContainingIgnoreCase(authorName);
    }
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    // Paginationnn and Sorting
    public Page<Book> getBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }
}
