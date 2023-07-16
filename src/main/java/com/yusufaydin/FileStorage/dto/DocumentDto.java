package com.yusufaydin.FileStorage.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DocumentDto {
    private String fileName;
    private String mimeType;
    private String fileContent;
    private String referenceSource;
    private String referenceKey;
    private String documentType;
    private List<String> tags;

}


