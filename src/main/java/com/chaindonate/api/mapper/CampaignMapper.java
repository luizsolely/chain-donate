package com.chaindonate.api.mapper;

import com.chaindonate.api.dto.CampaignRequestDTO;
import com.chaindonate.api.dto.CampaignResponseDTO;
import com.chaindonate.api.entity.Campaign;
import com.chaindonate.api.entity.Donation;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Mapper(componentModel = "spring")
public interface CampaignMapper {

    CampaignMapper INSTANCE = Mappers.getMapper(CampaignMapper.class);

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "donations", ignore = true)
    @Mapping(target = "user", ignore = true)
    Campaign toEntity(CampaignRequestDTO dto);

    @Mapping(target = "progressPercentage", expression = "java(calculateProgress(campaign))")
    @Mapping(target = "currentBalanceBTC", expression = "java(calculateCurrentBalance(campaign))")
    CampaignResponseDTO toDto(Campaign campaign);

    default BigDecimal calculateProgress(Campaign campaign) {
        BigDecimal current = calculateCurrentBalance(campaign);
        if (campaign.getGoalInBTC().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return current
                .multiply(BigDecimal.valueOf(100))
                .divide(campaign.getGoalInBTC(), 2, RoundingMode.DOWN);
    }

    default BigDecimal calculateCurrentBalance(Campaign campaign) {
        BigDecimal total = campaign.getDonations().stream()
                .filter(Donation::isConfirmed)
                .map(Donation::getAmountBTC)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return campaign.getInitialBalanceBTC().add(total);
    }
}
