package com.hrsystem.employee.unit;

import com.hrsystem.employee.controller.DepartmentController;
import com.hrsystem.employee.dto.DepartmentCreateDto;
import com.hrsystem.employee.dto.DepartmentDTO;
import com.hrsystem.employee.response.ApiResponse;
import com.hrsystem.employee.response.PageResponse;
import com.hrsystem.employee.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class DepartmentControllerUnitTest {

    @InjectMocks
    private DepartmentController controller;

    @Mock
    private DepartmentService service;

    private DepartmentDTO sampleDto;
    private DepartmentCreateDto createDto;

    @BeforeEach
    void setup() {
        createDto = DepartmentCreateDto.builder()
                .departmentName("HR")
                .description("Human Resources")
                .status(null)  // fill as needed
                .departmentCode("HR001")
                .build();

        sampleDto = DepartmentDTO.builder()
                .id(1L)
                .departmentName("HR")
                .description("Human Resources")
                .status(null)  // fill as needed
                .departmentCode("HR001")
                .build();
    }

    @Test
    void testCreate() {
        when(service.create(createDto)).thenReturn(sampleDto);

        ResponseEntity<ApiResponse<Long>> response = controller.create(createDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleDto.getId(), Objects.requireNonNull(response.getBody()).getData());

        verify(service, times(1)).create(createDto);
    }


    @Test
    void testGetDepartmentById() {
        when(service.findById(1L)).thenReturn(sampleDto);

        ResponseEntity<ApiResponse<DepartmentDTO>> response = controller.getDepartmentById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("HR", Objects.requireNonNull(response.getBody()).getData().getDepartmentName());

        verify(service, times(1)).findById(1L);
    }

    @Test
    void testGetAllDepartments() {
        Pageable pageable = PageRequest.of(0, 10);
        List<DepartmentDTO> list = List.of(sampleDto);
        PageResponse<DepartmentDTO> pageResponse = new PageResponse<>(list, 0, 10, 1, 1);

        when(service.findAll(null, pageable)).thenReturn(pageResponse);

        ResponseEntity<ApiResponse<PageResponse<DepartmentDTO>>> response = controller.getAllDepartments(0, 10, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(Objects.requireNonNull(response.getBody()).getData());
        assertEquals(1, response.getBody().getData().getContent().size());

        verify(service, times(1)).findAll(null, pageable);
    }

    @Test
    void testUpdate() {
        DepartmentDTO updatedDto = DepartmentDTO.builder()
                .id(1L)
                .departmentName("HR Updated")
                .description("Updated Description")
                .departmentCode("HR999")
                .build();

        when(service.update(1L, updatedDto)).thenReturn(updatedDto);

        ResponseEntity<ApiResponse<Long>> response = controller.update(1L, updatedDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDto.getId(), Objects.requireNonNull(response.getBody()).getData());

        verify(service, times(1)).update(1L, updatedDto);
    }

    @Test
    void testDelete() {
        when(service.delete(1L)).thenReturn(true);

        ResponseEntity<ApiResponse<Boolean>> response = controller.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(service, times(1)).delete(1L);
    }

}
