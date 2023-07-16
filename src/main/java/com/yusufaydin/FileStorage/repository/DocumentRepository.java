package com.yusufaydin.FileStorage.repository;


import com.yusufaydin.FileStorage.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Document findByDocumentKey(String documentKey);

    List<Document> findByReferenceSourceAndReferenceKey(String referenceSource, String referenceKey);
}