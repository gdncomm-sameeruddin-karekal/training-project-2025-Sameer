package com.blibli.member.exception;

import com.blibli.member.entityDTO.GenericResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(MemberNotFoundException.class)
        public GenericResponse<?> handleNotFound(MemberNotFoundException ex) {
            return GenericResponse.builder()
                    .status("ERROR")
                    .message(ex.getMessage())
                    .build();
        }

        @ExceptionHandler(RuntimeException.class)
        public GenericResponse<?> handleRuntime(RuntimeException ex) {
            return GenericResponse.builder()
                    .status("ERROR")
                    .message(ex.getMessage())
                    .build();
        }
}
