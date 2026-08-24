package com.example.springboardproject1.service;

import com.example.springboardproject1.dto.BookDTO;
import com.example.springboardproject1.model.Author;
import com.example.springboardproject1.model.Book;
import com.example.springboardproject1.model.BookCategory;
import com.example.springboardproject1.repository.AuthorRepository;
import com.example.springboardproject1.repository.BookRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository,
                       AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    // Create
    public Book createBook(BookDTO bookDTO) {

        if (bookRepository.existsByIsbn(bookDTO.getIsbn())) {
            throw new RuntimeException("ISBN already exists");
        }

        String url = "https://openlibrary.org/api/books"
                + "?bibkeys=ISBN:" + bookDTO.getIsbn()
                + "&format=json"
                + "&jscmd=data";

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> response =
                restTemplate.getForObject(url, Map.class);

        String authorName = null;

        if (response != null) {

            Map<String, Object> bookData =
                    (Map<String, Object>)
                            response.get("ISBN:" + bookDTO.getIsbn());

            if (bookData != null && bookData.get("authors") != null) {

                List<Map<String, Object>> authors =
                        (List<Map<String, Object>>) bookData.get("authors");

                if (!authors.isEmpty()) {
                    authorName = (String) authors.get(0).get("name");
                }
            }
        }

        if (authorName == null) {
            throw new RuntimeException(
                    "Author information was not found for this ISBN"
            );
        }
        Optional<Author> existingAuthor = authorRepository.findByName(authorName);

        Author author;

        if (existingAuthor.isPresent()) {
            author = existingAuthor.get();
        } else {
            author = new Author();
            author.setName(authorName);
            author = authorRepository.save(author);
        }

        Book book = new Book();

        book.setTitle(bookDTO.getTitle());
        book.setIsbn(bookDTO.getIsbn());
        book.setCategory(bookDTO.getCategory());
        book.setAuthor(author);
        book.setAvailable(true);

        return bookRepository.save(book);

    }

    // Read - all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Read - one book
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    // Update
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

    // Delete
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    // Search by title
    public List<Book> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    // Search by category
    public List<Book> searchByCategory(BookCategory category) {
        return bookRepository.findByCategory(category);
    }

    // Search by author
    public List<Book> searchByAuthor(String authorName) {
        return bookRepository
                .findByAuthorNameContainingIgnoreCase(authorName);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    // Pagination and Sorting
    public Page<Book> getBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }
}