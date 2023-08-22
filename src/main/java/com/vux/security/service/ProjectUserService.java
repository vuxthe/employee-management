package com.vux.security.service;

import com.vux.security.dto.ProjectUserDto;
import com.vux.security.entity.ProjectUser;

public interface ProjectUserService {
    public ProjectUserDto addToProject(
            Integer projectId,
            Integer userId,
            String role
    );

    public void removeUserInProject(
            Integer projectId,
            Integer userId
    );
}
