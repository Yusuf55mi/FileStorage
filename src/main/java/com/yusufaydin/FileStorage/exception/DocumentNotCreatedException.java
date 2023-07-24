package com.yusufaydin.FileStorage.exception;

import com.yusufaydin.FileStorage.enums.ExceptionTypes;

public class DocumentNotCreatedException extends FileStorageException {
    public DocumentNotCreatedException(String message) {
        super(message, ExceptionTypes.CREATION_ERROR, 400);
    }
}

