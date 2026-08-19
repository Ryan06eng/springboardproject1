package com.example.springboardproject1.repository;
import com.example.springboardproject1.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AuthorRepository extends  JpaRepository<Author,Long> {
}

