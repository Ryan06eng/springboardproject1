package com.example.springboardproject1.controller;

import com.example.springboardproject1.dto.BookDTO;
import com.example.springboardproject1.model.Book;
import com.example.springboardproject1.model.BookCategory;
import com.example.springboardproject1.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Book> createBook(
            @Valid @RequestBody BookDTO bookDTO) {

        return ResponseEntity.ok(
                bookService.createBook(bookDTO)
        );
    }

    // READ - all books
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    // READ - one book
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {

        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookDTO bookDTO) {

        try {

            Book bookDetails = new Book();

            bookDetails.setTitle(bookDTO.getTitle());
            bookDetails.setIsbn(bookDTO.getIsbn());
            bookDetails.setCategory(bookDTO.getCategory());
            bookDetails.setAvailable(bookDTO.isAvailable());

            return ResponseEntity.ok(
                    bookService.updateBook(id, bookDetails)
            );

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {

        bookService.deleteBook(id);

        return ResponseEntity.noContent().build();
    }

    // SEARCH BY TITLE
    @GetMapping("/search/title")
    public List<Book> searchByTitle(@RequestParam String title) {
        return bookService.searchByTitle(title);
    }

    // SEARCH BY CATEGORY
    @GetMapping("/search/category")
    public List<Book> searchByCategory(
            @RequestParam BookCategory category) {

        return bookService.searchByCategory(category);
    }

    // SEARCH BY AUTHOR
    @GetMapping("/search/author")
    public List<Book> searchByAuthor(
            @RequestParam String author) {

        return bookService.searchByAuthor(author);
    }

    // PAGINATION AND SORTING
    @GetMapping("/page")
    public Page<Book> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort;

        if (direction.equalsIgnoreCase("desc")) {
            sort = Sort.by(sortBy).descending();
        } else {
            sort = Sort.by(sortBy).ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        return bookService.getBooks(pageable);
    }
}
