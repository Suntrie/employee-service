package org.example;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
public class EmployeeControllerTest extends AbstractIntegrationTest{
    @Autowired
    private MockMvc mockMvc;

    @Nested
    @Transactional
    @DisplayName("getProject by id is working")
    @Sql(scripts = {"/db/user_data.sql"})
    class GetProject {

        @Test
        public void when_user_with_access_get_existing_project_by_id_expect_project() throws Exception {
            mockMvc.perform(get("/api/employees")
                            .with(getAuthentication("inga","ROLE_USER")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(hasSize(8)))
                    .andExpect(jsonPath("$[0].email").value("1@3"));
        }
    }
}
