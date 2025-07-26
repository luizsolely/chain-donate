package com.chaindonate.api.dto;

import java.util.List;

public record UserResponseDTO(

        Long id,
        String username,
        String email,
        List<SimpleCampaignDTO> campaigns

)
{
}
