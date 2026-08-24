package com.example.springboardproject1.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthorDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Biography is required")
    private String biography;

    public AuthorDTO() {
    }

    public AuthorDTO(String name, String biography) {
        this.name = name;
        this.biography = biography;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}
