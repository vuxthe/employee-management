package com.vux.security.service.impl;

import com.vux.security.dto.ProjectUserDto;
import com.vux.security.entity.ProjectUser;
import com.vux.security.mapper.ProjectUserMapper;
import com.vux.security.repository.ProjectRepository;
import com.vux.security.repository.ProjectUserRepository;
import com.vux.security.repository.UserRepository;
import com.vux.security.service.ProjectUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectUserServiceImpl implements ProjectUserService {
    private final ProjectUserRepository repository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    @Override
    public ProjectUserDto addToProject(
            Integer projectId,
            Integer userId,
            String role
    ) {
        ProjectUser entity = ProjectUser.builder()
                .project(projectRepository.findById(projectId).orElseGet(null))
                .user(userRepository.findById(userId).orElseGet(null))
                .roleInProject(role)
                .build();
        repository.save(entity);
        return ProjectUserMapper.MAPPER.mapToProjectUserDto(entity);
    }
    @Override
    public void removeUserInProject(
            Integer projectId,
            Integer userId
    ) {
        repository.removeUserInProject(projectId, userId);
    }
}
