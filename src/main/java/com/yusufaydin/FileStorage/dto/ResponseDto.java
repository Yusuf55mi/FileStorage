package com.yusufaydin.FileStorage.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseDto<DocumentDto> {
    private boolean success;
    private int errorCode;
    private List<String> errorList;
    private DocumentDto content;

}
