package com.example.springboardproject1.service;

import com.example.springboardproject1.model.Author;
import com.example.springboardproject1.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    // create it
    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    // Read - all authors
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    // Read - one author
    public Optional<Author> getAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    // update
    public Author updateAuthor(Long id, Author authorDetails) {

        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        author.setName(authorDetails.getName());
        author.setBiography(authorDetails.getBiography());

        return authorRepository.save(author);
    }

    // Delete
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }
}
