package com.chaindonate.api.auth.dto;

public record RegisterRequest(

        String username,
        String email,
        String password

)
{
}
