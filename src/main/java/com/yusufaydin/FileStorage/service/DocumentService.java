package com.yusufaydin.FileStorage.service;

import com.yusufaydin.FileStorage.dto.DocumentDto;
import com.yusufaydin.FileStorage.entity.Document;
import com.yusufaydin.FileStorage.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        Files.write(Path.of(filePath), fileContent);
        document.setFileContent(filePath);

        return documentRepository.save(document);
    }

    public DocumentDto getDocumentByKey(String documentKey) throws Exception {
        Document document = documentRepository.findByDocumentKey(documentKey);
        if (document == null) {
            throw new Exception("Document not found with key: " + documentKey);
        }

        return mapDocumentToDto(document);
    }

    public List<DocumentDto> getDocumentsByReference(String referenceSource, String referenceKey) throws Exception {
        List<Document> documents = documentRepository.findByReferenceSourceAndReferenceKey(referenceSource, referenceKey);
        if (documents.isEmpty()) {
            throw new Exception("Documents not found with references: " + referenceSource + "," + referenceKey);
        }

        return documents.stream()
                .map(this::mapDocumentToDto)
                .collect(Collectors.toList());
    }

    private DocumentDto mapDocumentToDto(Document document) {
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

    private byte[] decodeBase64(String base64String) {
        return Base64.getDecoder().decode(base64String.getBytes(StandardCharsets.UTF_8));
    }

    private String getFileExtension(String mimeType) throws Exception {
        return switch (mimeType) {
            case "application/pdf" -> ".pdf";
            case "text/plain" -> ".txt";
            case "text/html" -> ".html";
            case "application/xml" -> ".xml";
            case "application/zip" -> ".zip";
            case "application/msword" -> ".doc";
            case "image/png" -> ".png";
            case "application/vnd.ms-excel" -> ".xslx";
            default -> throw new Exception("Invalid file format.");
        };
    }
}
