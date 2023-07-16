package com.yusufaydin.FileStorage.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReferenceDto {
    private String referenceSource;
    private String referenceKey;

    public ReferenceDto() {
    }

    public ReferenceDto(String referenceSource, String referenceKey) {
        this.referenceSource = referenceSource;
        this.referenceKey = referenceKey;
    }

    }

