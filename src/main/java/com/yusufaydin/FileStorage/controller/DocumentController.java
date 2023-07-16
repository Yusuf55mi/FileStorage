package com.yusufaydin.FileStorage.controller;

import com.yusufaydin.FileStorage.dto.DocumentDto;
import com.yusufaydin.FileStorage.dto.ResponseDto;
import com.yusufaydin.FileStorage.entity.Document;
import com.yusufaydin.FileStorage.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;

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
                createdDocumentDto.setReferenceKey(document.getReferenceKey());
                createdDocumentDto.setDocumentType(document.getDocumentType());
                createdDocumentDto.setTags(document.getTags());
                createdDocumentDto.setMimeType(document.getMimeType());
                createdDocumentDto.setFileName(document.getFileName());
                createdDocumentDto.setFileContent(document.getFileContent());
                createdDocumentDto.setReferenceSource(document.getReferenceSource());

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


}
