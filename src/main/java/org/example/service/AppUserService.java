package org.example.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.security.JwtTokenProvider;
import org.example.domain.dto.UserVDTO;
import org.example.domain.model.AppUser;
import org.example.mappers.UserMapper;
import org.example.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public String login(String email, String password) {
        try {
            AppUser appUser = getByEmailOrThrow(email);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            return jwtTokenProvider.createToken(email, appUser.getAuthorities());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid username/password supplied");
        }
    }

    public UserVDTO whoami(HttpServletRequest req) {
        AppUser appUser = getByEmailOrThrow(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
        return userMapper.toVDTO(appUser);
    }

    public String refresh(String username) {
        return jwtTokenProvider.createToken(username, getByEmailOrThrow(username).getAuthorities());
    }

    public AppUser getByEmailOrThrow(String email){
        return appUserRepository.findByEmail(email)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist"));
    }

}
