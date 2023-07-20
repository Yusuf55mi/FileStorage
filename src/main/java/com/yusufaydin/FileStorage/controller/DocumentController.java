package com.yusufaydin.FileStorage.controller;

import com.yusufaydin.FileStorage.dto.DocumentDto;
import com.yusufaydin.FileStorage.dto.ReferenceDto;
import com.yusufaydin.FileStorage.dto.ResponseDto;
import com.yusufaydin.FileStorage.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/documents")
    public ResponseEntity<ResponseDto<String>> createDocument(@RequestBody DocumentDto documentDto) throws Exception {
        DocumentDto createdDocument = documentService.createDocument(documentDto);
        String documentKey = createdDocument.getDocumentKey();
        ResponseDto<String> response = createSuccessResponse(documentKey);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/getDocument")
    public ResponseEntity<ResponseDto<DocumentDto>> getDocument(@RequestBody DocumentDto documentKey) throws Exception {
        DocumentDto document = documentService.getDocumentByKey(documentKey.getDocumentKey());
        ResponseDto<DocumentDto> response = createSuccessResponse(document);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/getDocument/byReference")
    public ResponseEntity<ResponseDto<List<DocumentDto>>> getDocumentsByReference(@RequestBody ReferenceDto referenceDto) {
        List<DocumentDto> documents = documentService.getDocumentsByReference(referenceDto.getReferenceSource(), referenceDto.getReferenceKey());
        ResponseDto<List<DocumentDto>> response = createSuccessResponse(documents);
        return ResponseEntity.ok(response);
    }

    private <T> ResponseDto<T> createSuccessResponse(T content) {
        ResponseDto<T> response = new ResponseDto<>();
        response.setSuccess(true);
        response.setErrorCode(0);
        response.setErrorList(Collections.emptyList());
        response.setContent(content);
        return response;
    }
}
