package com.example.springboardproject1.controller;

import com.example.springboardproject1.dto.AuthorDTO;
import com.example.springboardproject1.dto.AuthorResponse;
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

    private AuthorResponse toResponse(Author author) {
        return new AuthorResponse(
                author.getId(),
                author.getName(),
                author.getBiography()
        );
    }

    // CREATE
    @PostMapping
    public ResponseEntity<AuthorResponse> createAuthor(
            @Valid @RequestBody AuthorDTO authorDTO) {

        Author author = new Author();

        author.setName(authorDTO.getName());
        author.setBiography(authorDTO.getBiography());

        Author createdAuthor = authorService.createAuthor(author);

        return ResponseEntity.ok(toResponse(createdAuthor));
    }

    // READ - all authors
    @GetMapping
    public List<AuthorResponse> getAllAuthors() {
        return authorService.getAllAuthors()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // READ - one author
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(
            @PathVariable Long id) {

        return authorService.getAuthorById(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponse> updateAuthor(
            @PathVariable Long id,
            @Valid @RequestBody AuthorDTO authorDTO) {

        Author authorDetails = new Author();

        authorDetails.setName(authorDTO.getName());
        authorDetails.setBiography(authorDTO.getBiography());

        try {
            Author updatedAuthor =
                    authorService.updateAuthor(id, authorDetails);

            return ResponseEntity.ok(toResponse(updatedAuthor));

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