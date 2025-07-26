package com.chaindonate.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SimpleDonationDTO(

        Long id,
        BigDecimal amountBTC,
        boolean confirmed,
        LocalDateTime createdAt

)
{
}
