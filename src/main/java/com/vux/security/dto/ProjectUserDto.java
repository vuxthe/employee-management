package com.vux.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectUserDto {
    private Integer projectId;
    private Integer userId;
    private String role;
}
