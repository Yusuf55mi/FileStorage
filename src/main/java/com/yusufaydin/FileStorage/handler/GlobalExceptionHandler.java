package com.yusufaydin.FileStorage.handler;

import com.yusufaydin.FileStorage.dto.ResponseDto;
import com.yusufaydin.FileStorage.enums.ExceptionTypes;
import com.yusufaydin.FileStorage.exception.FileStorageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ResponseDto<Object>> handleException(FileStorageException ex) {
        ResponseDto<Object> response = new ResponseDto<>();
        response.setSuccess(false);
        response.setErrorCode(ex.getCode());
        response.setErrorList(Collections.singletonList(ex.getMessage()));
        response.setContent(null);
        if (ex.getType() == ExceptionTypes.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if (ex.getType() == ExceptionTypes.CREATION_ERROR) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else if (ex.getType() == ExceptionTypes.DECODE_ERROR) {
            response.setErrorList(Collections.singletonList("Dosya decode edilemedi Base64 formatında olduğundan emin olunuz."));
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}

