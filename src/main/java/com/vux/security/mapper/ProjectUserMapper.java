package com.vux.security.mapper;

import com.vux.security.dto.ProjectUserDto;
import com.vux.security.entity.ProjectUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectUserMapper {
    ProjectUserMapper MAPPER = Mappers.getMapper(ProjectUserMapper.class);

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "roleInProject", target = "role")
    ProjectUserDto mapToProjectUserDto(ProjectUser projectUser);

}
