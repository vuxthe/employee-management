package com.vux.security.service.impl;


import com.vux.security.payload.UserResponse;
import com.vux.security.repository.UserRepository;
import com.vux.security.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private Connection connection;

    @Autowired
    private  UserRepository userRepository;

    @PostConstruct
    public void init() {
        System.out.println("Connecting...");

        try {
            connection = DriverManager.getConnection("jdbc:mysql://172.17.0.2:3306/management", "root", "root");
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection: " + e.getMessage());
        }
    }

    @PreDestroy
    public void cleanup() {
        System.out.println("Disconnecting...");

        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Failed to close database connection: " + e.getMessage());
        }
    }

    @Override
    public List<UserResponse> getUsers() {
        return userRepository.findUsers();
    }
}
