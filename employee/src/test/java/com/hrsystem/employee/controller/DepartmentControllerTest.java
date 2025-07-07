package com.hrsystem.employee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrsystem.employee.dto.DepartmentCreateDto;
import com.hrsystem.employee.dto.DepartmentDTO;
import com.hrsystem.employee.enums.DepartmentStatus;
import com.hrsystem.employee.repository.DepartmentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // 👈 Add this
class DepartmentControllerTest {

    private static final String TENANT_SCHEMA = "test_tenant";
    private static final String BASE_URL = "/api/v1/department";
    private Long createdDepartmentId;

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private DepartmentRepository repository;
    @Autowired private DataSource dataSource;

    @BeforeEach
    void setupSchema() throws Exception {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE SCHEMA IF NOT EXISTS " + TENANT_SCHEMA);

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS %s.departments (
                id BIGSERIAL PRIMARY KEY,
                department_name VARCHAR(255),
                description TEXT,
                status VARCHAR(50),
                department_code VARCHAR(255) UNIQUE NOT NULL,
                created_at TIMESTAMP,
                updated_at TIMESTAMP,
                is_active BOOLEAN,
                version BIGINT
            )
        """.formatted(TENANT_SCHEMA));

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS %s.audit_logs (
                    id BIGSERIAL PRIMARY KEY,
                    entity_name VARCHAR(255),
                    entity_id BIGINT,
                    action VARCHAR(100),
                    description TEXT,
                    performed_by VARCHAR(255),
                    timestamp TIMESTAMP
                )
            """.formatted(TENANT_SCHEMA));

        }
    }

    @Test
    @Order(1)
    void shouldCreateDepartmentSuccessfully() throws Exception {
        DepartmentCreateDto createDto = DepartmentCreateDto.builder()
                .departmentName("HR")
                .description("Human Resources")
                .status(DepartmentStatus.ACTIVE)
                .departmentCode("HR001")
                .build();

        String response = mockMvc.perform(post(BASE_URL)
                        .header("X-Tenant-ID", TENANT_SCHEMA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isNumber())
                .andReturn().getResponse().getContentAsString();

        createdDepartmentId = objectMapper.readTree(response).get("data").asLong();
        Assertions.assertNotNull(createdDepartmentId);
    }

    @Test
    @Order(2)
    void shouldGetDepartmentById() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + createdDepartmentId)
                        .header("X-Tenant-ID", TENANT_SCHEMA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.departmentName").value("HR"));
    }


    @Test
    @Order(3)
    void shouldGetAllDepartments() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .header("X-Tenant-ID", TENANT_SCHEMA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.data.content[0].departmentName", notNullValue()));

    }

    @Test
    @Order(4)
    void shouldUpdateDepartmentSuccessfully() throws Exception {
        DepartmentDTO updateDto = DepartmentDTO.builder()
                .departmentName("HR Updated")
                .description("Updated Description")
                .status(DepartmentStatus.INACTIVE)
                .departmentCode("HR999")
                .build();

        mockMvc.perform(put(BASE_URL + "/" + createdDepartmentId)
                        .header("X-Tenant-ID", TENANT_SCHEMA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(createdDepartmentId));
    }

    @Test
    @Order(5)
    void shouldDeleteDepartmentSuccessfully() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/1")
                        .header("X-Tenant-ID", TENANT_SCHEMA))
                .andExpect(status().isNoContent())
                .andReturn();

        // Verify deletion
        mockMvc.perform(get(BASE_URL + "/" + createdDepartmentId)
                        .header("X-Tenant-ID", TENANT_SCHEMA))
                .andExpect(status().isNotFound());
    }



    @AfterAll
    void dropSchema() throws Exception {
        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute("DROP SCHEMA IF EXISTS " + TENANT_SCHEMA + " CASCADE");
        }
    }


}
