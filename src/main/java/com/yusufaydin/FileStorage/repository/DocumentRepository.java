package com.yusufaydin.FileStorage.repository;


import com.yusufaydin.FileStorage.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Document findByDocumentKey(String documentKey);
}