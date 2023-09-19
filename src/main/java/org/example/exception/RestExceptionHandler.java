package org.example.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    private ResponseEntity<ErrorMessage> buildResponse(String exceptionMessage,
                                                       HttpStatusCode httpStatusCode,
                                                       Map<String, String> errors) {
        int code = httpStatusCode.value();

        var errorMessage = ErrorMessage.builder()
                .message(exceptionMessage)
                .errors(errors);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.name());
        return new ResponseEntity<>(errorMessage.build(), headers, code);
    }

    private ResponseEntity<ErrorMessage> buildResponse( HttpStatusCode httpStatusCode,
            String exceptionMessage) {
        return buildResponse(exceptionMessage, httpStatusCode, new HashMap<>());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage)); //TODO: default lang
        return buildResponse("Method argument not valid", HttpStatus.BAD_REQUEST,  errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorMessage> handleRestException(ResponseStatusException ex) {
        return buildResponse(ex.getStatusCode(), ex.getMessage());
    }
}
