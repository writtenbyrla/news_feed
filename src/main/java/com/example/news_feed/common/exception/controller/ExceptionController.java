package com.example.news_feed.common.exception.controller;

import com.example.news_feed.common.exception.dto.ExceptionResponse;
import com.example.news_feed.common.exception.HttpException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ExceptionResponse> handleHttpException(HttpException e) {
        HttpStatus httpStatus = e.getHttpStatus();
        String message = e.getMessage();
        return ResponseEntity.status(e.getHttpStatus()).body(
                new ExceptionResponse(
                        message,
                        httpStatus.value()
                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errorMessages = e.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        String message = errorMessages.toString();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(httpStatus).body(
                new ExceptionResponse(
                        message,
                        httpStatus.value()
                )
        );
    }

}
