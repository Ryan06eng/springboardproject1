package com.example.springboardproject1.dto;

import com.example.springboardproject1.model.BookCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @NotNull(message = "Category is required")
    private BookCategory category;

    @NotNull(message = "Author ID is required")
    private Long authorId;

    private boolean available;

    public BookDTO() {
    }

    public BookDTO(String title, String isbn, BookCategory category,
                   Long authorId, boolean available) {
        this.title = title;
        this.isbn = isbn;
        this.category = category;
        this.authorId = authorId;
        this.available = available;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public BookCategory getCategory() {
        return category;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}


