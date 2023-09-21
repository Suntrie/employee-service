package org.example.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.domain.dto.EmployeeCDTO;
import org.example.domain.dto.EmployeeLDTO;
import org.example.domain.dto.EmployeeUDTO;
import org.example.domain.dto.EmployeeVDTO;
import org.example.exception.ErrorMessage;
import org.example.service.EmployeeService;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee Controller", description = "Methods to control employees")
@RequiredArgsConstructor
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @Operation(description = "Create an employee")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New employee created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeVDTO.class))}),
            @ApiResponse(responseCode = "400", description = "The employee already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)))})
    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeVDTO createEmployee(@Parameter(description = "Employee's description") @Valid @RequestBody EmployeeCDTO employeeCDTO) {
        return employeeService.createEmployee(employeeCDTO);
    }

    @DeleteMapping("/{employeeId}")
    @Operation(description = "Delete an employee")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The employee deleted"),
            @ApiResponse(responseCode = "400", description = "The employee doesn't exist",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)))})
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEmployee(@Parameter(description = "Employee's id") @NotNull @PathVariable UUID employeeId) {
        employeeService.deleteEmployee(employeeId);
    }

    @PutMapping("/{employeeId}")
    @Operation(description = "Update an employee")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The employee updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeVDTO.class))}),
            @ApiResponse(responseCode = "400", description = "The employee doesn't exist",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatusException.class)))})
    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeVDTO updateEmployee(@Parameter(description = "Employee's id") @NotNull @PathVariable UUID employeeId,
                                       @Parameter(description = "New employee's description") @Valid @RequestBody EmployeeUDTO employeeUDTO) throws InterruptedException {
        return employeeService.updateEmployee(employeeId, employeeUDTO);
    }

    @GetMapping("/{employeeId}")
    @Operation(description = "Get an employee")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The employee received",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeVDTO.class))}),
            @ApiResponse(responseCode = "404", description = "The employee doesn't exist",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatusException.class)))})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public EmployeeVDTO getEmployee(@Parameter(description = "Employee's id") @NotNull @PathVariable UUID employeeId) {
        return employeeService.getEmployee(employeeId);
    }


    //TODO: validation - model, security, data types, OSIV, tests, readme + docker check + Kafka
    //Check lists mapping - save, update, delete
    //read transactional = ?
    //

    @GetMapping
    @Operation(description = "Get employees")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The employee received",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EmployeeLDTO.class)))})})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<EmployeeLDTO> getEmployees() {
        return employeeService.getEmployees();
    }
}