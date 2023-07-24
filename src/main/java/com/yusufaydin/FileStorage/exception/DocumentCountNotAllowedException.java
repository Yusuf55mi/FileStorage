package com.yusufaydin.FileStorage.exception;

import com.yusufaydin.FileStorage.enums.ExceptionTypes;

public class DocumentCountNotAllowedException extends FileStorageException {
    public DocumentCountNotAllowedException(String message) {
        super(message, ExceptionTypes.MAX_COUNT_ERROR, 405);
    }
}

