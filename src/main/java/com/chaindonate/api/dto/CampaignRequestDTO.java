package com.chaindonate.api.dto;

import java.math.BigDecimal;

public record CampaignRequestDTO(

        String title,
        String description,
        String btcAddress,
        BigDecimal goalInBTC

)
{
}
