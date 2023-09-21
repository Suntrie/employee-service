package org.example.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.model.UserRole;
import org.example.repository.AppUserRepository;
import org.example.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.junit.ClassRule;

import java.util.*;


//@ActiveProfiles("log4j2-spring.xml")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = AbstractIntegrationTest.DockerPostgresInitializer.class)
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

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    static {
        postgreDBContainer
                .withDatabaseName(DB_NAME)
                .withUsername(DB_USER)
                .withPassword(DB_PASSWORD)
                .withLogConsumer(new Slf4jLogConsumer(log))
                .withInitScript("db/extension.sql");

        postgreDBContainer.start();
    }


    public String getToken(String sub, List<UserRole> userRoles) {
        return jwtTokenProvider.createToken(sub, userRoles);
    }

    static class DockerPostgresInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
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
}
