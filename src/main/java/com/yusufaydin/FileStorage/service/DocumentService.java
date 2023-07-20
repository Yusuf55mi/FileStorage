package com.yusufaydin.FileStorage.service;

import com.yusufaydin.FileStorage.dto.DocumentDto;
import com.yusufaydin.FileStorage.entity.Document;
import com.yusufaydin.FileStorage.exception.DocumentNotCreatedException;
import com.yusufaydin.FileStorage.exception.DocumentNotDecodedException;
import com.yusufaydin.FileStorage.exception.DocumentNotFoundException;
import com.yusufaydin.FileStorage.mapper.DocumentMapper;
import com.yusufaydin.FileStorage.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.tomcat.util.codec.binary.Base64.encodeBase64;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    @Value("${file.upload.path}")
    private String path;

    public DocumentDto createDocument(DocumentDto documentDto) throws Exception {
        Document document = documentMapper.toDocument(documentDto);

        if (document.getFileName() == null) {
            throw new DocumentNotCreatedException("fileName boş bırakılamaz");
        } else if (document.getDocumentType() == null) {
            throw new DocumentNotCreatedException("documentType boş bırakılamaz");
        }

        document.setDocumentKey(UUID.randomUUID().toString());
        document.setCreatedAt(LocalDateTime.now());

        byte[] fileContent = decodeBase64(documentDto.getFileContent());

        if (fileContent == null) {
            throw new DocumentNotDecodedException("fileContent alanının base64 formatında olduğundan emin olunuz");
        }

        String filePath = path + document.getFileName();
        Files.write(Path.of(filePath), fileContent);
        document.setFileContent(filePath);

        document = documentRepository.save(document);

        return documentMapper.toDocumentDto(document);
    }

    public DocumentDto getDocumentByKey(String documentKey) {
        Document document = documentRepository.findByDocumentKey(documentKey);

        if (document == null) {
            throw new DocumentNotFoundException("Document not found with key: " + documentKey);
        }
        DocumentDto dto = documentMapper.toDocumentDto(document);
        String fileContent;
        try {
            fileContent = Base64.getEncoder().encodeToString(Files.readAllBytes(Path.of(document.getFileContent())));
        } catch (IOException e) {
            throw new DocumentNotDecodedException("Döküman Base64 formatına çevrilemedi");
        }
        dto.setFileContent(fileContent);
        return dto;
    }

    public List<DocumentDto> getDocumentsByReference(String referenceSource, String referenceKey) {
        List<Document> documents = documentRepository.findByReferenceSourceAndReferenceKey(referenceSource, referenceKey);

        if (documents.isEmpty()) {
            throw new DocumentNotFoundException("Documents not found with references: " + referenceSource + "," + referenceKey);
        }

        return documents.stream()
                .map(documentMapper::toDocumentDto)
                .collect(Collectors.toList());
    }

    private byte[] decodeBase64(String base64String) {
        return Base64.getDecoder().decode(base64String.getBytes(StandardCharsets.UTF_8));
    }
}
