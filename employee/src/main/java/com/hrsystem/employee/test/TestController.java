package com.hrsystem.employee.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/getUsers")
    public List<TestUser> getUsers() {
        String sql = "SELECT id, first_name, last_name, email, role, created_at FROM employee";

        return jdbcTemplate.query(sql, new RowMapper<TestUser>() {
            @Override
            public TestUser mapRow(ResultSet rs, int rowNum) throws SQLException {
                TestUser user = new TestUser();
                user.setId((UUID) rs.getObject("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                return user;
            }
        });
    }
}
