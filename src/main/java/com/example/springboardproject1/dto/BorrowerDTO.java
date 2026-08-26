package com.example.springboardproject1.dto;

import jakarta.validation.constraints.NotBlank;

public class BorrowerDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Contact information is required")
    private String contactInformation;

    public BorrowerDTO() {
    }

    public BorrowerDTO(String name, String contactInformation) {
        this.name = name;
        this.contactInformation = contactInformation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }
}