package com.vux.security.service;

import com.vux.security.dto.ProjectDto;
import com.vux.security.entity.Project;

import java.util.List;

public interface ProjectService {
    Project createProject(ProjectDto projectDto);
    ProjectDto getProject(Integer projectId);
    List<ProjectDto> getProjects();

    Project updateProject(Integer projectId, ProjectDto projectDto);
    void deleteProject(Integer projectId);


}
