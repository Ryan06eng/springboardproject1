package com.example.springboardproject1.dto;

public record BorrowerResponse(
        Long id,
        String name,
        String email,
        String phoneNumber
) {
}