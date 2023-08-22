package com.vux.security.mapper;

import com.vux.security.dto.CheckStatusDto;
import com.vux.security.entity.CheckStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CheckStatusMapper {
    CheckStatusMapper MAPPER = Mappers.getMapper(CheckStatusMapper.class);

    @Mapping(source = "user.id", target = "userId")
    CheckStatusDto mapToCheckStatusDto(CheckStatus checkStatus);

}
