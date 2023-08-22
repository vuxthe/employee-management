package com.vux.security.service.impl;

import com.vux.security.dto.ProjectDto;
import com.vux.security.mapper.ProjectMapper;
import com.vux.security.repository.ProjectRepository;
import com.vux.security.entity.Project;
import com.vux.security.mapper.MapStructMapper;
import com.vux.security.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    @Override
    public Project createProject(ProjectDto projectDto) {
        Project entity = Project.builder()
                .name(projectDto.getName())
                .start(projectDto.getStart())
                .expectedEnd(projectDto.getEnd())
                .description(projectDto.getDescription())
                .build();
        return projectRepository.save(entity);
    }

    @Override
    public ProjectDto getProject(Integer projectId) {
        return ProjectMapper.MAPPER.mapToProjectDto(projectRepository.findById(projectId).get());
    }

    @Override
    public List<ProjectDto> getProjects() {
        return projectRepository.findAll().stream().map(ProjectMapper.MAPPER::mapToProjectDto).collect(Collectors.toList());
    }
    @Override
    public Project updateProject(Integer projectId, ProjectDto dto) {
        Project entity = projectRepository.findById(projectId).get();
        entity.setName(dto.getName());
        entity.setStart(dto.getStart());
        entity.setExpectedEnd(dto.getEnd());
        entity.setDescription(dto.getDescription());
        return projectRepository.save(entity);
    }

    @Override
    public void deleteProject(Integer projectId) {
        projectRepository.deleteById(projectId);
    }

}
