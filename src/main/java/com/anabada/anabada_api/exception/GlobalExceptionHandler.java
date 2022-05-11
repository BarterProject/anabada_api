package com.anabada.anabada_api.exception;

import com.anabada.anabada_api.domain.message.dto.MessageDTO;
import javassist.NotFoundException;
import javassist.bytecode.DuplicateMemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<MessageDTO> NotFoundException(NotFoundException e){
        log.error(e.getMessage());
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchFileException.class)
    public ResponseEntity<MessageDTO> NoSuchFileException(NoSuchFileException e){
        log.error(e.getMessage());
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<MessageDTO> IllegalAccessException(IllegalAccessException e){
        log.error(e.getMessage());
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<MessageDTO> DuplicateMemberException(DuplicateMemberException e){
        log.error(e.getMessage());
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotSupportedException.class)
    public ResponseEntity<MessageDTO> NotSupportedException(NotSupportedException e){
        log.error(e.getMessage());
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(NotAcceptableStatusException.class)
    public ResponseEntity<MessageDTO> NotAcceptableStatusException(NotAcceptableStatusException e){
        log.error(e.getMessage());
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageDTO> MethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error(e.getMessage());
        return new ResponseEntity<>(new MessageDTO(e.getAllErrors().get(0).getDefaultMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<MessageDTO> IOException(IOException e){
        log.error(e.getMessage());
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<MessageDTO> BadCredentialsException(BadCredentialsException e){
        log.error(e.getMessage());
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageDTO> Exception(Exception e){
        log.error(e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }





}

