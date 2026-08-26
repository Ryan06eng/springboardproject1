package com.example.springboardproject1.controller;

import com.example.springboardproject1.dto.BookDTO;
import com.example.springboardproject1.dto.BookResponse;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Convert Book entity to BookResponse DTO
    private BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getCategory(),
                book.getAuthor() != null ? book.getAuthor().getName() : null,
                book.isAvailable()
        );
    }

    // CREATE
    @PostMapping
    public ResponseEntity<BookResponse> createBook(
            @Valid @RequestBody BookDTO bookDTO) {

        Book book = bookService.createBook(bookDTO);

        return ResponseEntity.ok(toResponse(book));
    }

    // READ - all books
    @GetMapping
    public List<BookResponse> getAllBooks() {

        return bookService.getAllBooks()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // READ - one book
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(
            @PathVariable Long id) {

        return bookService.getBookById(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookDTO bookDTO) {

        Book bookDetails = new Book();

        bookDetails.setTitle(bookDTO.getTitle());
        bookDetails.setIsbn(bookDTO.getIsbn());
        bookDetails.setCategory(bookDTO.getCategory());
        bookDetails.setAvailable(bookDTO.isAvailable());

        Book updatedBook = bookService.updateBook(id, bookDetails);

        return ResponseEntity.ok(toResponse(updatedBook));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Long id) {

        bookService.deleteBook(id);

        return ResponseEntity.noContent().build();
    }

    // SEARCH BY TITLE
    @GetMapping("/search/title")
    public List<BookResponse> searchByTitle(
            @RequestParam String title) {

        return bookService.searchByTitle(title)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // SEARCH BY CATEGORY
    @GetMapping("/search/category")
    public List<BookResponse> searchByCategory(
            @RequestParam BookCategory category) {

        return bookService.searchByCategory(category)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // SEARCH BY AUTHOR
    @GetMapping("/search/author")
    public List<BookResponse> searchByAuthor(
            @RequestParam String author) {

        return bookService.searchByAuthor(author)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // PAGINATION AND SORTING
    @GetMapping("/page")
    public Page<BookResponse> getBooks(
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

        return bookService.getBooks(pageable)
                .map(this::toResponse);
    }
}