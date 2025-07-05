package com.hrsystem.employee.test;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class TestUser {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private Timestamp createdAt;
}
