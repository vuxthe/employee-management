package com.vux.security.testService;

import com.vux.security.payload.UserResponse;
import com.vux.security.repository.UserRepository;
import com.vux.security.service.UserService;
import com.vux.security.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)

public class TestService {

    @TestConfiguration
    public static class TestServiceConfiguration{
        @Bean
        UserServiceImpl userService() {
            return new UserServiceImpl();
        }
    }
    @MockBean
    UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Before
    public void setup() {
        List<UserResponse> userList = new LinkedList<>();
        userList.add(new UserResponse("user1", "email1@mail.com"));
        userList.add(new UserResponse("user2", "email2@mail.com"));
        userList.add(new UserResponse("user3", "email3@mail.com"));
        userList.add(new UserResponse("user4", "email4@mail.com"));

        Mockito.when(userRepository.findUsers()).thenReturn(userList);
    }

    @Test
    public void testCount() {
        List<UserResponse> userResponseList = userService.getUsers();
        UserResponse user1 = userResponseList.get(0);
        Assert.assertEquals(user1.getName(), "user1");
    }
}
