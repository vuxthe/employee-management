package com.vux.security.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;



@AllArgsConstructor
@Getter
@Setter
public class UserResponse {
    private String name;
    private String email;
}
