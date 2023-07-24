package com.yusufaydin.FileStorage.exception;

import com.yusufaydin.FileStorage.enums.ExceptionTypes;

public class DocumentMimeTypeNotAllowedException extends FileStorageException {
    public DocumentMimeTypeNotAllowedException(String message) {
        super(message, ExceptionTypes.MIME_TYPE_ERROR, 405);
    }
}

