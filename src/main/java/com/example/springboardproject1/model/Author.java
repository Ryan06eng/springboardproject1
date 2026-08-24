package com.example.springboardproject1.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import com.example.springboardproject1.model.Book;
import java.util.List;
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Author name is mandatory")
    private String name;

    private String biography;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books;

    public Author() {}

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getBiography() {return biography;}
    public void setBiography(String biography) {this.biography = biography;}

    public List<Book> getBooks() {return books;}
    public void setBooks(List<Book> books) {this.books = books;}
}

