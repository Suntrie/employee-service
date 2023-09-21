package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDTO {

    @Schema(description = "User's email")
    @NotBlank
    @Email
    private String username;

    @NotBlank
    @Schema(description = "User's password")
    private String password;
}
