/*
package org.example.unit;

import org.example.domain.model.AppUser;
import org.example.domain.model.UserRole;
import org.example.repository.AppUserRepository;
import org.example.security.AppUserDetailsService;
import org.example.security.JwtTokenProvider;
import org.example.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.log4j2-spring.xml.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@DisplayName("EmployeeService is working when")
@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    @InjectMocks
    @Spy
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    AppUserDetailsService appUserDetailsService;

    @Mock
    AppUserRepository appUserRepository;

    @Test
    void when_get_authentication_from_valid_token(){

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQb2xpbmFAc3R1IiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XSwiaWF0IjoxNjk1Mjg2MzU2LCJleHAiOjE2OTUyODk5NTZ9.248CDMaOzomiPfrAGTbBWToklM_ld-eojzSCRCUfurc";
        String username = "Polina@stu";

        AppUser appUser = new AppUser();
        appUser.setAuthorities(List.of(UserRole.ROLE_USER));
        appUser.setEmail(username);

        //when(jwtTokenProvider.getUsername("log4j2-spring.xml")).thenReturn("Polina@stu");
        when(appUserRepository.findByEmail(username)).thenReturn(Optional.of(appUser));
        //captor=?
        Authentication resultAuthentication = jwtTokenProvider.getAuthentication(token);
        Authentication modelAuthentication =
                new UsernamePasswordAuthenticationToken(appUser.getUsername(), "", appUser.getAuthorities());
        //appUserRepository.findByEmail(username)
        assertEquals(modelAuthentication, resultAuthentication);
    }
    */
/*
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = appUserDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }*//*


}
*/
