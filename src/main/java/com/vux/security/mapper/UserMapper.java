package com.vux.security.mapper;

import com.vux.security.dto.CheckStatusDto;
import com.vux.security.dto.UserDto;
import com.vux.security.entity.CheckStatus;
import com.vux.security.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    UserDto mapToUserDto(User user);

}
