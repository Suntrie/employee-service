package org.example.web;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.domain.dto.UserLoginDTO;
import org.example.domain.dto.UserVDTO;
import org.example.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "Methods to control user access")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final AppUserService appUserService;

    @PostMapping("/signin")
    @Operation(description = "Get token")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "User doesn't exist",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatusException.class))),
            @ApiResponse(responseCode = "422", description = "Invalid login/pass",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatusException.class)))})
    public String login(@Parameter(description = "User's credentials") @Valid @RequestBody UserLoginDTO userLoginDTO){
        return appUserService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());
    }

    @GetMapping(value = "/me")
    @Operation(description = "Get current user")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "User doesn't exist",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatusException.class)))})
    public UserVDTO whoami(HttpServletRequest req) {
        return appUserService.whoami(req);
    }

    @Operation(description = "Refresh token")
    @GetMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "User doesn't exist",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatusException.class)))})
    public String refresh(HttpServletRequest req) {
        return appUserService.refresh(req.getRemoteUser());
    }

}
