package org.example.security;

import lombok.RequiredArgsConstructor;
import org.example.domain.model.AppUser;
import org.example.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final AppUser appUser = appUserRepository.findByEmail(username)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist"));

        return User
                .withUsername(username)
                .password(appUser.getPassword())
                .authorities(appUser.getAuthorities())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

}
