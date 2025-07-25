package com.chaindonate.api.mapper;

import com.chaindonate.api.entity.User;
import com.chaindonate.api.dto.UserResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toDTO(User user);
}
