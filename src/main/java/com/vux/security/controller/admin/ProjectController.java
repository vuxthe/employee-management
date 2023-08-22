package com.vux.security.controller.admin;

import com.vux.security.dto.ProjectDto;
import com.vux.security.dto.ProjectUserDto;
import com.vux.security.entity.Project;
import com.vux.security.service.ProjectService;
import com.vux.security.service.ProjectUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectUserService projectUserService;

    @GetMapping("")
    public List<ProjectDto> getProjects() {
        return projectService.getProjects();
    }

    @GetMapping("/{projectId}")
    public ProjectDto getProject(
            @PathVariable Integer projectId
    ) {
        return projectService.getProject(projectId);
    }
    @PostMapping("")
    public Project create(
            @RequestBody ProjectDto projectDto
    ) {
        return projectService.createProject(projectDto);
    }

    @PutMapping
    public Project update(
            @RequestParam Integer projectId,
            @RequestBody ProjectDto projectDto
    ) {
        return projectService.updateProject(projectId, projectDto);
    }

    @DeleteMapping
    public void delete(
            @RequestParam Integer projectId
    ) {
        projectService.deleteProject(projectId);
    }

    @PostMapping("/add-user")
    public ProjectUserDto addUserInProject(
            @RequestParam Integer userId,
            @RequestParam Integer projectId,
            @RequestParam String role
    ) {
        return projectUserService.addToProject(projectId, userId, role);
    }

    @DeleteMapping("remove-user")
    public void removeUserInProject(
            @RequestParam Integer userId,
            @RequestParam Integer projectId
    ) {
        projectUserService.removeUserInProject(projectId, userId);
    }
}
