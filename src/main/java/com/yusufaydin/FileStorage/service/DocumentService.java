package com.yusufaydin.FileStorage.service;

import com.yusufaydin.FileStorage.dto.DocumentDto;
import com.yusufaydin.FileStorage.entity.Document;
import com.yusufaydin.FileStorage.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document createDocument(DocumentDto documentDto) throws Exception {
        Document document = new Document();
        document.setFileName(documentDto.getFileName());
        document.setMimeType(documentDto.getMimeType());
        document.setReferenceSource(documentDto.getReferenceSource());
        document.setReferenceKey(documentDto.getReferenceKey());
        document.setDocumentType(documentDto.getDocumentType());
        document.setDocumentKey(UUID.randomUUID().toString());
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());
        document.setTags(documentDto.getTags());

        byte[] fileContent = decodeBase64(documentDto.getFileContent());

        String fileExtension = getFileExtension(document.getMimeType());
        String filePath = "src/main/java/com/yusufaydin/FileStorage/fileContent/" + document.getDocumentKey() + fileExtension;
        try {
            Files.write(Path.of(filePath), fileContent);
            document.setFileContent(filePath);
        } catch (Exception e) {
            throw new Exception("Failed to save file: " + e.getMessage());
        }

        return documentRepository.save(document);
    }

    public DocumentDto getDocumentByKey(String documentKey) throws Exception {
        Document document = documentRepository.findByDocumentKey(documentKey);
        if (document == null) {
            throw new Exception("Document not found with key: " + documentKey);
        }

        DocumentDto documentDto = new DocumentDto();
        documentDto.setFileName(document.getFileName());
        documentDto.setMimeType(document.getMimeType());
        documentDto.setReferenceSource(document.getReferenceSource());
        documentDto.setReferenceKey(document.getReferenceKey());
        documentDto.setDocumentKey(document.getDocumentKey());
        documentDto.setDocumentType(document.getDocumentType());
        documentDto.setFileContent(document.getFileContent());

        return documentDto;
    }

    public List<DocumentDto> getDocumentsByReference(String referenceSource, String referenceKey) throws IOException {
        List<Document> documents = documentRepository.findByReferenceSourceAndReferenceKey(referenceSource, referenceKey);
        List<DocumentDto> documentDtos = new ArrayList<>();

        for (Document document : documents) {
            DocumentDto documentDto = new DocumentDto();
            documentDto.setFileName(document.getFileName());
            documentDto.setMimeType(document.getMimeType());
            documentDto.setReferenceSource(document.getReferenceSource());
            documentDto.setReferenceKey(document.getReferenceKey());
            documentDto.setDocumentKey(document.getDocumentKey());
            documentDto.setDocumentType(document.getDocumentType());
            documentDto.setFileContent(document.getFileContent());
            documentDtos.add(documentDto);
        }

        return documentDtos;
    }

    private byte[] decodeBase64(String base64String) {
        return Base64.getDecoder().decode(base64String.getBytes(StandardCharsets.UTF_8));
    }

    private String encodeBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private String getFileExtension(String mimeType) throws Exception {
        if (mimeType.equals("application/pdf")) {
            return ".pdf";
        } else if (mimeType.equals("text/plain")) {
            return ".txt";
        } else if (mimeType.equals("text/html")) {
            return ".html";
        } else if (mimeType.equals("application/xml")) {
            return ".xml";
        } else if (mimeType.equals("application/zip")) {
            return ".zip";
        } else if (mimeType.equals("application/msword")) {
            return ".doc";
        } else if (mimeType.equals("image/png")) {
            return ".png";
        } else if (mimeType.equals("application/vnd.ms-excel")) {
            return ".xml";
        } else {
            throw new Exception("Invalid file format.");
        }
    }
}
