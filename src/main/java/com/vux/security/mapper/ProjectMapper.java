package com.vux.security.mapper;

import com.vux.security.dto.ProjectDto;
import com.vux.security.entity.Comment;
import com.vux.security.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProjectMapper {
    ProjectMapper MAPPER = Mappers.getMapper(ProjectMapper.class);

    @Mapping(source = "expectedEnd", target = "end")
    @Mapping(target = "commentList", expression = "java(mapComment(project))")
    ProjectDto mapToProjectDto(Project project);

    default List<String> mapComment(Project project) {
        return project.getCommentList()
                .stream().map(Comment::getContent)
                .collect(Collectors.toList());
    }
}
