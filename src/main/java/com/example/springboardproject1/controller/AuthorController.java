package com.example.springboardproject1.controller;

import com.example.springboardproject1.dto.AuthorDTO;
import com.example.springboardproject1.model.Author;
import com.example.springboardproject1.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // CREATE
    @PostMapping
    public Author createAuthor(@Valid @RequestBody AuthorDTO authorDTO) {

        Author author = new Author();

        author.setName(authorDTO.getName());
        author.setBiography(authorDTO.getBiography());

        return authorService.createAuthor(author);
    }

    // READ - all authors
    @GetMapping
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    // READ - one author
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {

        return authorService.getAuthorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(
            @PathVariable Long id,
            @Valid @RequestBody AuthorDTO authorDTO) {

        Author authorDetails = new Author();

        authorDetails.setName(authorDTO.getName());
        authorDetails.setBiography(authorDTO.getBiography());

        try {
            return ResponseEntity.ok(
                    authorService.updateAuthor(id, authorDetails)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {

        authorService.deleteAuthor(id);

        return ResponseEntity.noContent().build();
    }
}