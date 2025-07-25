package com.chaindonate.api.mapper;

import com.chaindonate.api.dto.UserResponseDTO;
import com.chaindonate.api.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponseDTO toDto(User user);
}
