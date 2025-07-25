package com.chaindonate.api.auth.dto;

import com.chaindonate.api.dto.UserResponseDTO;

public record AuthResponse(

        String token,
        UserResponseDTO user

)
{
}
