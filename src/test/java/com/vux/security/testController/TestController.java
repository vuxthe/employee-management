package com.vux.security.testController;

import com.vux.security.SecurityApplication;
import com.vux.security.controller.PublicController;
import com.vux.security.payload.UserResponse;
import com.vux.security.service.UserService;
import com.vux.security.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(PublicController.class)
public class TestController {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    public void testFindUsers() throws Exception {
        List<UserResponse> userList = new LinkedList<>();
        userList.add(new UserResponse("user1", "email1@mail.com"));
        userList.add(new UserResponse("user2", "email2@mail.com"));
        userList.add(new UserResponse("user3", "email3@mail.com"));
        userList.add(new UserResponse("user4", "email4@mail.com"));

        given(userService.getUsers()).willReturn(userList);

        mockMvc.perform(get("/api/v1/public/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].name", is("user1")))
                .andExpect(jsonPath("$[0].email", is("email1@gmail.com")));
    }
}
