package org.example;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.model.UserRole;
import org.example.repository.AppUserRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.junit.ClassRule;

import java.util.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;


//@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = AbstractIntegrationTest.DockerPostgreNSIDataInitializer.class)
@AutoConfigureMockMvc
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@Slf4j
public abstract class AbstractIntegrationTest {
    private static final String POSTGRES_CURRENT_VERSION = "postgres:12.7";
    private static final String DB_USER = "krst_user";
    private static final String DB_NAME = "krst_db";
    private static final String DB_PASSWORD = "886-P&W-5FB";

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected AppUserRepository appUserRepository;

    @ClassRule
    public static PostgreSQLContainer<?> postgreDBContainer = new PostgreSQLContainer<>(POSTGRES_CURRENT_VERSION);

    static {
        postgreDBContainer
                .withDatabaseName(DB_NAME)
                .withUsername(DB_USER)
                .withPassword(DB_PASSWORD)
                .withLogConsumer(new Slf4jLogConsumer(log))
                .withInitScript("db/extension.sql");

        postgreDBContainer.start();
    }


    public static RequestPostProcessor getAuthentication(String sub, String employeeRights, String... authorities) {
     /*   Map<String, Object> authorityAttributes = new HashMap<>();
        authorityAttributes.put("score", "openid");
        GrantedAuthority authority = new OAuth2UserAuthority(authorityAttributes);

        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        grantedAuthorityList.add(authority);
        Arrays.stream(authorities).forEach(s -> grantedAuthorityList.add(new SimpleGrantedAuthority(s)));

        JSONArray employeeRightsArray = new JSONArray();
        employeeRightsArray.add(employeeRights);*/

/*        var mock = Mockito.mock(Jwt.class);
        Mockito.when(mock.getClaims()).thenReturn(Map.of("sub", sub, "rights", employeeRightsArray));

        var mockRepo = Mockito.mock(UserRepository.class);

        Mockito.when(mockRepo.findByFirstName("inga")).thenReturn((User)User.builder()
                .firstName("inga").build());*/

        List<UserRole> roles = List.of(UserRole.ROLE_USER);
        UserDetails userDetails =
        org.springframework.security.core.userdetails.User
                .withUsername("inga")
                .password("$2a$08$ZlPH72xaNv4/GprW1rgHgeMOc0q3EOEH1dM9PUtFch3HUYq0PyxD6")
                .authorities(roles)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
//new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        // List<UserRole> "ROLE_USER"
        return authentication(new UsernamePasswordAuthenticationToken(userDetails,"",
                roles));
    }

    static class DockerPostgreNSIDataInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @SneakyThrows
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreDBContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreDBContainer.getUsername(),
                    "spring.datasource.password=" + postgreDBContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    protected MockHttpServletRequestBuilder requestAsUser(MockHttpServletRequestBuilder request, String username) {
        return username == null ? request : request
                .with(getAuthentication(username,"ROLE_USER"));
    }
}
