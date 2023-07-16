package com.yusufaydin.FileStorage.service;

import com.yusufaydin.FileStorage.dto.DocumentDto;
import com.yusufaydin.FileStorage.entity.Document;
import com.yusufaydin.FileStorage.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document createDocument(DocumentDto documentDto) {
        Document document = new Document();
        document.setFileName(documentDto.getFileName());
        document.setMimeType(documentDto.getMimeType());
        document.setFileContent(documentDto.getFileContent());
        document.setReferenceSource(documentDto.getReferenceSource());
        document.setReferenceKey(documentDto.getReferenceKey());
        document.setDocumentType(documentDto.getDocumentType());
        document.setDocumentKey(UUID.randomUUID().toString());
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());
        document.setTags(documentDto.getTags());
        return documentRepository.save(document);
    }
}
