package org.example.web;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.example.domain.dto.UserVDTO;
import org.example.mappers.UserMapper;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "Methods to control user access")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    //TODO: swagger api
    @PostMapping("/signin")
    @Operation(description = "Get token")
    public String login(
            @Parameter(description = "Username") @RequestParam String username,
            @Parameter(description = "Password") @RequestParam String password) {
        return userService.signin(username, password);
    }

    @Operation(description = "Get current user")
    @GetMapping(value = "/me")
    public UserVDTO whoami(HttpServletRequest req) {
        return userMapper.toVDTO(userService.whoami(req));
    }

    @Operation(description = "Refresh token")
    @GetMapping("/refresh")
    public String refresh(HttpServletRequest req) {
        return userService.refresh(req.getRemoteUser());
    }

}
