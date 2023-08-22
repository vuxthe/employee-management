package com.vux.security.service;


import com.vux.security.payload.UserResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;


public interface UserService {
    List<UserResponse> getUsers();
}
