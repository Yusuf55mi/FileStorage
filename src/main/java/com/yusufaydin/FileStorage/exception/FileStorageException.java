package com.yusufaydin.FileStorage.exception;

import com.yusufaydin.FileStorage.enums.ExceptionTypes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileStorageException extends RuntimeException {
    private ExceptionTypes type;
    private int code;

    public FileStorageException(String message, ExceptionTypes type, int code) {
        super(message);
        this.type = type;
        this.code = code;
    }

}

