package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class EmployeeVDTO {

    @Schema(description = "Employee's id")
    private UUID id;

    @Schema(description = "Employee's first name")
    private String firstName;

    @Schema(description = "Employee's last name")
    private String lastName;

    @Schema(description = "Employee's email")
    private String email;

    @Schema(description = "Employee's birthday")
    private Date birthday;

    @Schema(description = "Employee's hobbies")
    private List<String> hobbies;
}
