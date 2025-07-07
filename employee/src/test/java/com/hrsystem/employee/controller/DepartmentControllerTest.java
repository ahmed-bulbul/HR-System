package com.hrsystem.employee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrsystem.employee.dto.DepartmentCreateDto;
import com.hrsystem.employee.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

@SpringBootTest
@AutoConfigureMockMvc
class DepartmentControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private DepartmentRepository repository;
    @Autowired private DataSource dataSource;

    private static final String TENANT_ID = "tenant_test_001";

    private DepartmentCreateDto createDto;




}
