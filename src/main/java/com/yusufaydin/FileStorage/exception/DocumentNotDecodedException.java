package com.yusufaydin.FileStorage.exception;

import com.yusufaydin.FileStorage.enums.ExceptionTypes;

public class DocumentNotDecodedException extends FileStorageException {
    public DocumentNotDecodedException(String message) {
        super(message, ExceptionTypes.DECODE_ERROR, 424);
    }
}

