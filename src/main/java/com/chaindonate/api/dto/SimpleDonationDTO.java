package com.chaindonate.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SimpleDonationDTO(

        Long id,

        @JsonSerialize(using = ToStringSerializer.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00000000")
        BigDecimal amountBTC,

        boolean confirmed,
        LocalDateTime createdAt

)
{
}
