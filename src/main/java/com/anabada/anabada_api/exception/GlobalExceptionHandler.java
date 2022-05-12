package com.anabada.anabada_api.exception;

import com.anabada.anabada_api.domain.message.dto.MessageDTO;
import javassist.NotFoundException;
import javassist.bytecode.DuplicateMemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.NotAcceptableStatusException;

import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiExceptionEntity> ApiException(ApiException e){
        log.error("Api Exception " + e.toString());
        ApiExceptionEntity apiExceptionEntity = ApiExceptionEntity.builder()
                .errorCode(e.getError().getCode())
                .errorMessage(e.getError().getMessage())
                .build();

        return new ResponseEntity<>(apiExceptionEntity, e.getError().getStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiExceptionEntity> ApiException(HttpMessageNotReadableException e){
        log.error("Api Exception " + e.toString());
        ApiExceptionEntity apiExceptionEntity = ApiExceptionEntity.builder()
                .errorCode(ExceptionEnum.RUNTIME_EXCEPTION.getCode())
                .errorMessage("invalid input type")
                .build();

        return new ResponseEntity<>(apiExceptionEntity, ExceptionEnum.RUNTIME_EXCEPTION.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiExceptionEntity> MethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error(e.getMessage());
        ApiExceptionEntity apiExceptionEntity = ApiExceptionEntity.builder()
                .errorCode(ExceptionEnum.RUNTIME_EXCEPTION_VALID_ERROR.getCode())
                .errorMessage(ExceptionEnum.RUNTIME_EXCEPTION_VALID_ERROR.getMessage())
                .build();

        return new ResponseEntity<>(apiExceptionEntity, ExceptionEnum.RUNTIME_EXCEPTION_VALID_ERROR.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageDTO> Exception(Exception e){
        log.error(e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }





}

