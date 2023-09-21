package org.example.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class ErrorMessage {
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> errors;
}
