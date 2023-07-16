package com.yusufaydin.FileStorage.controller;

import com.yusufaydin.FileStorage.dto.DocumentDto;
import com.yusufaydin.FileStorage.dto.ReferenceDto;
import com.yusufaydin.FileStorage.dto.ResponseDto;
import com.yusufaydin.FileStorage.entity.Document;
import com.yusufaydin.FileStorage.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
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

            if (document != null) {
                DocumentDto createdDocumentDto = new DocumentDto();
                createdDocumentDto.setDocumentKey(document.getDocumentKey());

                ResponseDto<DocumentDto> response = new ResponseDto<>();
                response.setSuccess(true);
                response.setErrorCode(0);
                response.setErrorList(Collections.emptyList());
                response.setContent(createdDocumentDto);

                return ResponseEntity.ok(response);
            } else {
                throw new Exception("Document creation failed.");
            }
        } catch (Exception e) {
            ResponseDto<DocumentDto> response = new ResponseDto<>();
            response.setSuccess(false);
            response.setErrorCode(e.hashCode());
            response.setErrorList(Arrays.asList(e.getMessage()));
            response.setContent(null);

            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/getDocument")
    public ResponseEntity<ResponseDto<DocumentDto>> getDocument(@RequestBody String documentKey) {
        try {
            DocumentDto document = documentService.getDocumentByKey(documentKey);

            if (document != null) {
                DocumentDto documentDto = new DocumentDto();
                documentDto.setFileName(document.getFileName());
                documentDto.setMimeType(document.getMimeType());
                documentDto.setFileContent(document.getFileContent());
                documentDto.setReferenceSource(document.getReferenceSource());
                documentDto.setReferenceKey(document.getReferenceKey());
                documentDto.setDocumentType(document.getDocumentType());

                ResponseDto<DocumentDto> response = new ResponseDto<>();
                response.setSuccess(true);
                response.setErrorCode(0);
                response.setErrorList(Collections.emptyList());
                response.setContent(documentDto);

                return ResponseEntity.ok(response);
            } else {
                throw new Exception("Document not found.");
            }
        } catch (Exception e) {
            ResponseDto<DocumentDto> response = new ResponseDto<>();
            response.setSuccess(false);
            response.setErrorCode(e.hashCode());
            response.setErrorList(Arrays.asList(e.getMessage()));
            response.setContent(null);

            return ResponseEntity.ok(response);
        }
    }
    @PostMapping("/getDocument/byReference")
    public ResponseEntity<ResponseDto<List<DocumentDto>>> getDocumentsByReference(@RequestBody ReferenceDto referenceDto) {
        try {
        List<DocumentDto> documents = documentService.getDocumentsByReference(referenceDto.getReferenceSource(), referenceDto.getReferenceKey());
            if (documents != null) {
        List<DocumentDto> documentDtos = new ArrayList<>();
        for (DocumentDto document : documents) {
            DocumentDto documentDto = new DocumentDto();
            documentDto.setFileName(document.getFileName());
            documentDto.setMimeType(document.getMimeType());
            documentDto.setReferenceSource(document.getReferenceSource());
            documentDto.setReferenceKey(document.getReferenceKey());
            documentDto.setDocumentKey(document.getDocumentKey());
            documentDto.setDocumentType(document.getDocumentType());
            documentDtos.add(documentDto);
        }

        ResponseDto<List<DocumentDto>> response = new ResponseDto<>();
        response.setSuccess(true);
        response.setErrorCode(0);
        response.setErrorList(Collections.emptyList());
        response.setContent(documentDtos);

        return ResponseEntity.ok(response);
            } else {
                throw new Exception("Document not found.");
            }
        } catch (Exception e) {
            ResponseDto<List<DocumentDto>> response = new ResponseDto<>();
            response.setSuccess(false);
            response.setErrorCode(e.hashCode());
            response.setErrorList(Arrays.asList(e.getMessage()));
            response.setContent(null);

            return ResponseEntity.ok(response);
        }
    }

}
