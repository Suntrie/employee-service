package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class EmployeeLDTO {
    @Schema(description = "Employee's first name")
    private String firstName;

    @Schema(description = "Employee's last name")
    private String lastName;

    @Schema(description = "Employee's email")
    private String email;

    @Schema(description = "Employee's birthday")
    private Date birthday;
}
