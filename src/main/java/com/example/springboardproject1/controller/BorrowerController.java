package com.example.springboardproject1.controller;

import com.example.springboardproject1.dto.BorrowerDTO;
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

    // CREATE
    @PostMapping
    public Borrower createBorrower(
            @Valid @RequestBody BorrowerDTO borrowerDTO) {

        Borrower borrower = new Borrower();

        borrower.setName(borrowerDTO.getName());
        borrower.setContactInformation(
                borrowerDTO.getContactInformation()
        );

        return borrowerService.createBorrower(borrower);
    }

    // READ - all borrowers
    @GetMapping
    public List<Borrower> getAllBorrowers() {
        return borrowerService.getAllBorrowers();
    }

    // READ - one borrower
    @GetMapping("/{id}")
    public ResponseEntity<Borrower> getBorrowerById(
            @PathVariable Long id) {

        return borrowerService.getBorrowerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Borrower> updateBorrower(
            @PathVariable Long id,
            @Valid @RequestBody BorrowerDTO borrowerDTO) {

        Borrower borrowerDetails = new Borrower();

        borrowerDetails.setName(borrowerDTO.getName());
        borrowerDetails.setContactInformation(
                borrowerDTO.getContactInformation()
        );

        try {
            return ResponseEntity.ok(
                    borrowerService.updateBorrower(
                            id,
                            borrowerDetails
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrower(
            @PathVariable Long id) {

        borrowerService.deleteBorrower(id);

        return ResponseEntity.noContent().build();
    }
}
