package com.yusufaydin.FileStorage.exception;

import com.yusufaydin.FileStorage.enums.ExceptionTypes;

public class DocumentSizeNotAllowedException extends FileStorageException {
    public DocumentSizeNotAllowedException(String message) {
        super(message, ExceptionTypes.MAX_SIZE_ERROR, 405);
    }
}

