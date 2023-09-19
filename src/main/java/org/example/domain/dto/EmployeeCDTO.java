package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import java.util.Date;
import java.util.List;

@Data
public class EmployeeCDTO {

    @Schema(description = "Employee's first name")
    @NotBlank
    private String firstName;

    @Schema(description = "Employee's last name")
    @NotBlank
    private String lastName;

    @Schema(description = "Employee's email")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "Employee's birthday")
    @NotNull
    private Date birthday;

    @Schema(description = "Employee's hobbies")
    @NotNull
    private List<@NotBlank String> hobbies;
}
