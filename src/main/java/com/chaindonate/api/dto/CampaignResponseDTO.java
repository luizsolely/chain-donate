package com.chaindonate.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CampaignResponseDTO(

        Long id,
        String title,
        String description,
        String btcAddress,
        BigDecimal goalInBTC,
        BigDecimal initialBalanceBTC,
        BigDecimal currentBalanceBTC,
        BigDecimal progressPercentage,
        LocalDateTime createdAt

)
{
}
