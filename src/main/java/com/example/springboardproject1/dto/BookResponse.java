package com.example.springboardproject1.dto;

import com.example.springboardproject1.model.BookCategory;

public record BookResponse(
        Long id,
        String title,
        String isbn,
        BookCategory category,
        String authorName,
        boolean available
) {
}
