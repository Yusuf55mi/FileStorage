package com.yusufaydin.FileStorage.service;

import com.yusufaydin.FileStorage.dto.DocumentDto;
import com.yusufaydin.FileStorage.entity.Document;
import com.yusufaydin.FileStorage.entity.DocumentType;
import com.yusufaydin.FileStorage.exception.*;
import com.yusufaydin.FileStorage.mapper.DocumentMapper;
import com.yusufaydin.FileStorage.repository.DocumentRepository;
import com.yusufaydin.FileStorage.repository.DocumentTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final DocumentTypeRepository documentTypeRepository;
    @Value("${file.upload.path}")
    private String path;

    public DocumentDto createDocument(DocumentDto documentDto) throws Exception {
        Document document = documentMapper.toDocument(documentDto);

        document.setDocumentKey(UUID.randomUUID().toString());
        document.setCreatedAt(LocalDateTime.now());

        validateDocument(document);
        validateDocumentType(documentDto);

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
            fileContent = encodeBase64(Files.readAllBytes(Path.of(document.getFileContent())));
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

    private void validateDocument(Document document) {
        Class<Document> documentClass = Document.class;
        Field[] fields = documentClass.getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(document);

                if (value == null) {
                    throw new DocumentNotCreatedException(field.getName() + " boş bırakılamaz");
                } else if (value instanceof String && ((String) value).isEmpty()) {
                    throw new DocumentNotCreatedException(field.getName() + " boş bırakılamaz");
                }
            } catch (IllegalAccessException e) {
                throw new DocumentNotCreatedException("Döküman doğrulanırken bir hata meydana geldi");
            }
        }
    }

    private void validateDocumentType(DocumentDto documentDto) {
        DocumentType documentType = documentTypeRepository.findDocumentTypeByNameContains(documentDto.getDocumentType());
        if (!documentType.getAllowedMimeTypes().contains(documentDto.getMimeType())) {
            throw new DocumentMimeTypeNotAllowedException(documentDto.getDocumentType() + " tipi dökümanlar " + documentType.getAllowedMimeTypes() + " şeklinde kayıt edilebilir.");
        }
        byte[] file = decodeBase64(documentDto.getFileContent());
        long fileSizeInBytes = file.length;
        double fileSizeInMegabytes = (double) fileSizeInBytes / (1024 * 1024);
        if (fileSizeInMegabytes > documentType.getMaxFileSize()) {
            throw new DocumentSizeNotAllowedException(documentDto.getDocumentType() + " tipi dökümanlar en fazla " + documentType.getMaxFileSize() + "MB boyutunda olabilir.");
        }
        List<DocumentDto> documents = getDocumentsByReference(documentDto.getReferenceSource(), documentDto.getReferenceKey());
        int documentCount = documents.size();
        if (documentCount > documentType.getMaxFileCount()) {
            throw new DocumentCountNotAllowedException(documentType.getName() + " tipi dökümanlardan en fazla " + documentType.getMaxFileCount() + " adet bulunabilir.");
        }
    }

    private byte[] decodeBase64(String base64String) {
        return Base64.getDecoder().decode(base64String.getBytes(StandardCharsets.UTF_8));
    }

    private String encodeBase64(byte[] fileContent) {
        return Base64.getEncoder().encodeToString(fileContent);
    }
}
