package org.example.configuration;

import lombok.RequiredArgsConstructor;
import org.example.security.JwtTokenFilterConfigurer;
import org.example.security.JwtTokenProvider;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.server.ResponseStatusException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authConfiguration;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable()
                .authorizeHttpRequests(authorizeHttpRequests ->
                {
                    try {
                        authorizeHttpRequests
                                .requestMatchers(EndpointRequest.to(HealthEndpoint.class)).anonymous()
                                .requestMatchers("/api/users/signin").permitAll()
                                .requestMatchers("/error").permitAll()
                                .requestMatchers("/api/employees/**").permitAll()
                                .anyRequest().authenticated()
                        ;
                    } catch (Exception e) {
                        throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
                    }
                });

        http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web
                .ignoring()
                .requestMatchers("/swagger-ui.html", "/swagger-resources/**", "/swagger-ui/**",
                        "/open-api-docs/**","/api/users/signin", "/actuator/health");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authConfiguration.getAuthenticationManager();
    }


}
