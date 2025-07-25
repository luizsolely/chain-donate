package com.chaindonate.api.auth.dto;

public record LoginRequest(

        String email,
        String password

)
{
}
