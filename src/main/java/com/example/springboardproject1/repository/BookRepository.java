package com.example.springboardproject1.repository;

import com.example.springboardproject1.model.Book;
import com.example.springboardproject1.model.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByCategory(BookCategory category);

    List<Book> findByAuthorNameContainingIgnoreCase(String name);


    boolean existsByIsbn(String isbn);    ;

}