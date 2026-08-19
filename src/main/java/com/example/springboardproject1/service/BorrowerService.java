package com.example.springboardproject1.service;


import com.example.springboardproject1.model.Borrower;
import com.example.springboardproject1.repository.BorrowerRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;

    public BorrowerService(BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    // CREATE
    public Borrower createBorrower(Borrower borrower) {
        return borrowerRepository.save(borrower);
    }

    // READ - all borrowers
    public List<Borrower> getAllBorrowers() {
        return borrowerRepository.findAll();
    }

    // READ - one borrower
    public Optional<Borrower> getBorrowerById(Long id) {
        return borrowerRepository.findById(id);
    }

    // UPDATE
    public Borrower updateBorrower(Long id, Borrower borrowerDetails) {

        Borrower borrower = borrowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));

        borrower.setName(borrowerDetails.getName());
        borrower.setContactInformation(
                borrowerDetails.getContactInformation()
        );

        return borrowerRepository.save(borrower);
    }

    // DELETE
    public void deleteBorrower(Long id) {
        borrowerRepository.deleteById(id);
    }
}
