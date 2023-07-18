package com.yusufaydin.FileStorage.controller;

import com.yusufaydin.FileStorage.dto.DocumentDto;
import com.yusufaydin.FileStorage.dto.ReferenceDto;
import com.yusufaydin.FileStorage.dto.ResponseDto;
import com.yusufaydin.FileStorage.entity.Document;
import com.yusufaydin.FileStorage.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/documents")
    public ResponseEntity<ResponseDto<DocumentDto>> createDocument(@RequestBody DocumentDto documentDto) {
        try {
            Document document = documentService.createDocument(documentDto);

            DocumentDto createdDocumentDto = new DocumentDto();
            createdDocumentDto.setDocumentKey(document.getDocumentKey());

            ResponseDto<DocumentDto> response = createSuccessResponse(createdDocumentDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDto<DocumentDto> response = createErrorResponse(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/getDocument")
    public ResponseEntity<ResponseDto<DocumentDto>> getDocument(@RequestBody DocumentDto documentKey) {
        try {
            DocumentDto document = documentService.getDocumentByKey(documentKey.getDocumentKey());

            ResponseDto<DocumentDto> response = createSuccessResponse(document);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDto<DocumentDto> response = createErrorResponse(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/getDocument/byReference")
    public ResponseEntity<ResponseDto<List<DocumentDto>>> getDocumentsByReference(@RequestBody ReferenceDto referenceDto) {
        try {
            List<DocumentDto> documents = documentService.getDocumentsByReference(referenceDto.getReferenceSource(), referenceDto.getReferenceKey());

            ResponseDto<List<DocumentDto>> response = createSuccessResponse(documents);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDto<List<DocumentDto>> response = createErrorResponse(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private <T> ResponseDto<T> createSuccessResponse(T content) {
        ResponseDto<T> response = new ResponseDto<>();
        response.setSuccess(true);
        response.setErrorCode(0);
        response.setErrorList(Collections.emptyList());
        response.setContent(content);
        return response;
    }

    private <T> ResponseDto<T> createErrorResponse(Exception e) {
        ResponseDto<T> response = new ResponseDto<>();
        response.setSuccess(false);
        response.setErrorCode(e.hashCode());
        response.setErrorList(Collections.singletonList(e.getMessage()));
        response.setContent(null);
        return response;
    }
}
