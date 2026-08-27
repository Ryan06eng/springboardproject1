package com.example.springboardproject1.controller;

import com.example.springboardproject1.dto.BorrowerDTO;
import com.example.springboardproject1.dto.BorrowerResponse;
import com.example.springboardproject1.model.Borrower;
import com.example.springboardproject1.service.BorrowerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {

    private final BorrowerService borrowerService;

    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    private BorrowerResponse toResponse(Borrower borrower) {
        return new BorrowerResponse(
                borrower.getId(),
                borrower.getName(),
                borrower.getEmail(),
                borrower.getPhoneNumber()
        );
    }

    // CREATE
    @PostMapping
    public ResponseEntity<BorrowerResponse> createBorrower(
            @Valid @RequestBody BorrowerDTO borrowerDTO) {

        Borrower borrower = new Borrower();

        borrower.setName(borrowerDTO.getName());
        borrower.setEmail(borrowerDTO.getEmail());
        borrower.setPhoneNumber(borrowerDTO.getPhoneNumber());

        Borrower createdBorrower =
                borrowerService.createBorrower(borrower);

        return ResponseEntity.ok(toResponse(createdBorrower));
    }

    // READ - all borrowers
    @GetMapping
    public List<BorrowerResponse> getAllBorrowers() {
        return borrowerService.getAllBorrowers()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // READ - one borrower
    @GetMapping("/{id}")
    public ResponseEntity<BorrowerResponse> getBorrowerById(
            @PathVariable Long id) {

        return borrowerService.getBorrowerById(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<BorrowerResponse> updateBorrower(
            @PathVariable Long id,
            @Valid @RequestBody BorrowerDTO borrowerDTO) {

        Borrower borrowerDetails = new Borrower();

        borrowerDetails.setName(borrowerDTO.getName());
        borrowerDetails.setEmail(borrowerDTO.getEmail());
        borrowerDetails.setPhoneNumber(borrowerDTO.getPhoneNumber());

        Borrower updatedBorrower =
                borrowerService.updateBorrower(id, borrowerDetails);

        return ResponseEntity.ok(toResponse(updatedBorrower));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrower(
            @PathVariable Long id) {

        borrowerService.deleteBorrower(id);

        return ResponseEntity.noContent().build();
    }
}