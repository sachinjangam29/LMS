package com.jangam.schoolmgt.userservice.exceptions;

import com.jangam.schoolmgt.userservice.payload.response.MessageResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptions {


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleEnumConversionExceptions(MethodArgumentTypeMismatchException e) {
        if(e.getCause() instanceof IllegalArgumentException){
            String errorMessage = "Invalid value for Role: " + e.getValue();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Invalid request", HttpStatus.BAD_REQUEST);
     }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MessageResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException e,
                                                               HttpServletRequest request) {
        String errorMessage = "Invalid input. Please check the request body. " + e.getMostSpecificCause().getMessage();

        MessageResponse err = MessageResponse.builder()
                .message(errorMessage)
                .status(HttpStatus.BAD_REQUEST)
                .path(request.getServletPath())
                .build();

        return new ResponseEntity<>(err,HttpStatus.BAD_REQUEST);
    }
}
