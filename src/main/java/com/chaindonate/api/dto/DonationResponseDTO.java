package com.chaindonate.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DonationResponseDTO(

        String txHash,
        String fromAddress,
        BigDecimal amountBTC,
        LocalDateTime timestamp

)
{
}
