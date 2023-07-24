package com.yusufaydin.FileStorage.exception;

import com.yusufaydin.FileStorage.enums.ExceptionTypes;

public class DocumentNotFoundException extends FileStorageException {
    public DocumentNotFoundException(String message) {
        super(message, ExceptionTypes.NOT_FOUND, 404);
    }
}

