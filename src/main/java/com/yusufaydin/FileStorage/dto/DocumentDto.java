package com.yusufaydin.FileStorage.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentDto {
    private String fileName;
    private String mimeType;
    private String fileContent;
    private String referenceSource;
    private String referenceKey;
    private String documentKey;
    private String documentType;
    private List<String> tags;

}


