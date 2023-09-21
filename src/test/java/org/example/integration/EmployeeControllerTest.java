package org.example.integration;


import org.example.domain.dto.EmployeeCDTO;
import org.example.domain.dto.EmployeeUDTO;
import org.example.domain.model.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("EmployeeController is working when")
public class EmployeeControllerTest extends AbstractIntegrationTest{
    @Autowired
    private MockMvc mockMvc;

    @Nested
    @Transactional
    @DisplayName("getEmployees is working")
    @Sql(scripts = {"/db/user_data.sql", "/db/employee_data.sql"})
    class GetEmployees{
        @Test
        void when_unauthorized_user_get_employees() throws Exception {
            mockMvc.perform(get("/api/employees"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void when_authorized_user_with_role_user_get_employees() throws Exception {
            mockMvc.perform(get("/api/employees")
                            .header(HttpHeaders.AUTHORIZATION,
                                    String.format("Bearer %s",
                                            getToken("1@2", List.of(UserRole.ROLE_USER)))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(hasSize(2)))
                    .andExpect(jsonPath("$[0].email").value("1@3"));
        }

        @Test
        void when_authorized_user_with_role_admin_get_employees() throws Exception {
            mockMvc.perform(get("/api/employees")
                            .header(HttpHeaders.AUTHORIZATION,
                                    String.format("Bearer %s",
                                            getToken("x@y", List.of(UserRole.ROLE_ADMIN)))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(hasSize(2)))
                    .andExpect(jsonPath("$[0].email").value("1@3"));
        }
    }

    @Nested
    @Transactional
    @DisplayName("updateEmployee is working")
    @Sql(scripts = {"/db/user_data.sql", "/db/employee_data.sql"})
    class UpdateEmployee{
        @Test
        void when_unauthorized_user_update_employee() throws Exception {
            mockMvc.perform(put("/api/employees/74a7e555-744b-48cc-b5c5-f56f2661c795"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void when_authorized_user_with_role_admin_update_existing_employee_without_email() throws Exception {

            EmployeeUDTO employeeUDTO = EmployeeUDTO.builder()
                    .firstName("K")
                    .birthday(new Date())
                    .lastName("1")
                    .email("1@3")
                    .hobbies(List.of())
                    .build();

            mockMvc.perform(put("/api/employees/74a7e555-744b-48cc-b5c5-f56f2661c795")
                            .header(HttpHeaders.AUTHORIZATION,
                                    String.format("Bearer %s",
                                            getToken("x@y", List.of(UserRole.ROLE_ADMIN))))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(employeeUDTO)))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstName").value("K"))
                    .andExpect(jsonPath("$.email").value("1@3"));
        }


        @Test
        void when_authorized_user_with_role_admin_update_existing_employee_with_email() throws Exception {

            EmployeeUDTO employeeUDTO = EmployeeUDTO.builder()
                    .firstName("K")
                    .birthday(new Date())
                    .lastName("1")
                    .email("1@xyz")
                    .hobbies(List.of())
                    .build();

            mockMvc.perform(put("/api/employees/74a7e555-744b-48cc-b5c5-f56f2661c795")
                            .header(HttpHeaders.AUTHORIZATION,
                                    String.format("Bearer %s",
                                            getToken("x@y", List.of(UserRole.ROLE_ADMIN))))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(employeeUDTO)))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstName").value("K"))
                    .andExpect(jsonPath("$.email").value("1@xyz"));
        }

        @Test
        void when_authorized_user_with_role_admin_update_not_existing_employee() throws Exception {

            EmployeeUDTO employeeUDTO = EmployeeUDTO.builder()
                    .firstName("K")
                    .birthday(new Date())
                    .lastName("1")
                    .email("1@3")
                    .hobbies(List.of())
                    .build();


            mockMvc.perform(put("/api/employees/74a7e555-744b-48cc-b5c5-f56f26bbc795")
                            .header(HttpHeaders.AUTHORIZATION,
                                    String.format("Bearer %s",
                                            getToken("x@y", List.of(UserRole.ROLE_ADMIN))))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(employeeUDTO)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void when_authorized_user_with_role_user_update_employee() throws Exception {

            EmployeeUDTO employeeUDTO = EmployeeUDTO.builder()
                    .firstName("K")
                    .birthday(new Date())
                    .lastName("1")
                    .email("1@3")
                    .hobbies(List.of())
                    .build();

            mockMvc.perform(put("/api/employees/74a7e555-744b-48cc-b5c5-f56f2661c795")
                            .header(HttpHeaders.AUTHORIZATION,
                                    String.format("Bearer %s",
                                            getToken("1@2", List.of(UserRole.ROLE_USER))))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(employeeUDTO)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void when_authorized_user_with_role_admin_update_employee_duplicate_email() throws Exception {

            EmployeeUDTO employeeUDTO = EmployeeUDTO.builder()
                    .firstName("K")
                    .birthday(new Date())
                    .lastName("1")
                    .email("1@4")
                    .hobbies(List.of())
                    .build();

            mockMvc.perform(put("/api/employees/74a7e555-744b-48cc-b5c5-f56f2661c795")
                            .header(HttpHeaders.AUTHORIZATION,
                                    String.format("Bearer %s",
                                            getToken("x@y", List.of(UserRole.ROLE_ADMIN))))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(employeeUDTO)))

                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    @Transactional
    @DisplayName("createEmployee is working")
    @Sql(scripts = {"/db/user_data.sql", "/db/employee_data.sql"})
    class CreateEmployee{
        @Test
        void when_unauthorized_user_create_employee() throws Exception {
            mockMvc.perform(post("/api/employees"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void when_authorized_user_with_role_admin_create_employee() throws Exception {

            EmployeeCDTO employeeUDTO = EmployeeCDTO.builder()
                    .firstName("K")
                    .birthday(new Date())
                    .lastName("1")
                    .email("1@10")
                    .hobbies(List.of("reading", "skating"))
                    .build();

            mockMvc.perform(post("/api/employees")
                            .header(HttpHeaders.AUTHORIZATION,
                                    String.format("Bearer %s",
                                            getToken("x@y", List.of(UserRole.ROLE_ADMIN))))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(employeeUDTO)))

                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.email").value("1@10"))
                    .andExpect(jsonPath("$.firstName").value("K"))
                    .andExpect(jsonPath("$.hobbies").value(hasItems("reading", "skating")));
        }

        @Test
        void when_authorized_user_with_role_user_create_employee() throws Exception {

            EmployeeCDTO employeeUDTO = EmployeeCDTO.builder()
                    .firstName("K")
                    .birthday(new Date())
                    .lastName("1")
                    .email("1@10")
                    .hobbies(List.of("reading", "skating"))
                    .build();

            mockMvc.perform(post("/api/employees")
                            .header(HttpHeaders.AUTHORIZATION,
                                    String.format("Bearer %s",
                                            getToken("1@2", List.of(UserRole.ROLE_USER))))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(employeeUDTO)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void when_authorized_user_with_role_admin_create_employee_duplicate_email() throws Exception {

            EmployeeCDTO employeeUDTO = EmployeeCDTO.builder()
                    .firstName("K")
                    .birthday(new Date())
                    .lastName("1")
                    .email("1@4")
                    .hobbies(List.of("reading", "skating"))
                    .build();

            mockMvc.perform(post("/api/employees")
                            .header(HttpHeaders.AUTHORIZATION,
                                    String.format("Bearer %s",
                                            getToken("x@y", List.of(UserRole.ROLE_ADMIN))))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(employeeUDTO)))

                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    @Transactional
    @DisplayName("deleteEmployee is working")
    @Sql(scripts = {"/db/user_data.sql", "/db/employee_data.sql"})
    class DeleteEmployee{

        @Test
        void when_unauthorized_user_delete_employee() throws Exception {
            mockMvc.perform(post("/api/employees"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void when_authorized_user_with_role_admin_delete_existing_employee() throws Exception {

            mockMvc.perform(get("/api/employees")
                            .header(HttpHeaders.AUTHORIZATION,
                                    String.format("Bearer %s",
                                            getToken("x@y", List.of(UserRole.ROLE_ADMIN)))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(hasSize(2)))
                    .andExpect(jsonPath("$[*].id").value(hasItems("74a7e555-744b-48cc-b5c5-f56f2661c795",
                            "84a7e555-744b-48cc-b5c5-f56f2661c795")));

            mockMvc.perform(delete("/api/employees/74a7e555-744b-48cc-b5c5-f56f2661c795")
                            .header(HttpHeaders.AUTHORIZATION,
                                    String.format("Bearer %s",
                                            getToken("x@y", List.of(UserRole.ROLE_ADMIN)))))
                    .andExpect(status().isNoContent());

            mockMvc.perform(get("/api/employees")
                            .header(HttpHeaders.AUTHORIZATION,
                                    String.format("Bearer %s",
                                            getToken("x@y", List.of(UserRole.ROLE_ADMIN)))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(hasSize(1)))
                    .andExpect(jsonPath("$[*].id").value(hasItems("84a7e555-744b-48cc-b5c5-f56f2661c795")));

        }

        @Test
        void when_authorized_user_with_role_admin_delete_not_existing_employee() throws Exception {

            mockMvc.perform(delete("/api/employees/74a7e555-744b-48cc-b5c5-f56f2661c7dd")
                            .header(HttpHeaders.AUTHORIZATION,
                                    String.format("Bearer %s",
                                            getToken("x@y", List.of(UserRole.ROLE_ADMIN)))))
                    .andExpect(status().isNotFound());
        }

        @Test
        void when_authorized_user_with_role_user_delete_employee() throws Exception {

            mockMvc.perform(delete("/api/employees/74a7e555-744b-48cc-b5c5-f56f2661c795")
                            .header(HttpHeaders.AUTHORIZATION,
                                    String.format("Bearer %s",
                                            getToken("1@2", List.of(UserRole.ROLE_USER)))))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @Transactional
    @DisplayName("getEmployee is working")
    @Sql(scripts = {"/db/user_data.sql", "/db/employee_data.sql"})
    class GetEmployee{
        @Test
        void when_unauthorized_user_get_employee() throws Exception {
            mockMvc.perform(get("/api/employees/74a7e555-744b-48cc-b5c5-f56f2661c795"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void when_authorized_user_with_role_user_get_existing_employee() throws Exception {
            mockMvc.perform(get("/api/employees/74a7e555-744b-48cc-b5c5-f56f2661c795")
                            .header(HttpHeaders.AUTHORIZATION,
                                    String.format("Bearer %s",
                                            getToken("1@2", List.of(UserRole.ROLE_USER)))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("74a7e555-744b-48cc-b5c5-f56f2661c795"))
                    .andExpect(jsonPath("$.hobbies").value(hasItems("reading", "knitting")));
        }

        @Test
        void when_authorized_user_with_role_admin_get_existing_employee() throws Exception {
            mockMvc.perform(get("/api/employees/74a7e555-744b-48cc-b5c5-f56f2661c795")
                            .header(HttpHeaders.AUTHORIZATION,
                                    String.format("Bearer %s",
                                            getToken("x@y", List.of(UserRole.ROLE_ADMIN)))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("74a7e555-744b-48cc-b5c5-f56f2661c795"))
                    .andExpect(jsonPath("$.hobbies").value(hasItems("reading", "knitting")));

        }

        @Test
        void when_authorized_user_with_role_admin_get_not_existing_employees() throws Exception {
            mockMvc.perform(get("/api/employees/74a7e555-744b-48cc-b5c5-f56f2661dd95")
                            .header(HttpHeaders.AUTHORIZATION,
                                    String.format("Bearer %s",
                                            getToken("x@y", List.of(UserRole.ROLE_ADMIN)))))
                    .andExpect(status().isNotFound());

        }
    }

}
