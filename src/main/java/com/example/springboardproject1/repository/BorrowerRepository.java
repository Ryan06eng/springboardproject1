package com.example.springboardproject1.repository;

import com.example.springboardproject1.model.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {
}
