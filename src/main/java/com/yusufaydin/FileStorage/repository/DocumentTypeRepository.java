package com.yusufaydin.FileStorage.repository;


import com.yusufaydin.FileStorage.entity.Document;
import com.yusufaydin.FileStorage.entity.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
    DocumentType findDocumentTypeByName(String name);

    DocumentType findDocumentTypeByNameContains(String name);

}