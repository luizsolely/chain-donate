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

    default Campaign toEntity(CampaignRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Campaign campaign = new Campaign();
        campaign.setTitle(dto.title());
        campaign.setDescription(dto.description());
        campaign.setBtcAddress(dto.btcAddress());
        campaign.setGoalInBTC(dto.goalInBTC());
        return campaign;
    }

    default CampaignResponseDTO toDto(Campaign campaign) {
        if (campaign == null) {
            return null;
        }

        BigDecimal currentBalance = calculateCurrentBalance(campaign);
        BigDecimal progress = calculateProgress(campaign);

        return new CampaignResponseDTO(
                campaign.getId(),
                campaign.getTitle(),
                campaign.getDescription(),
                campaign.getBtcAddress(),
                campaign.getGoalInBTC(),
                campaign.getInitialBalanceBTC(),
                currentBalance,
                progress,
                campaign.getCreatedAt()
        );
    }

    default BigDecimal calculateProgress(Campaign campaign) {
        BigDecimal current = calculateCurrentBalance(campaign);
        if (campaign.getGoalInBTC() == null || campaign.getGoalInBTC().compareTo(BigDecimal.ZERO) == 0) {
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

        BigDecimal initial = campaign.getInitialBalanceBTC() == null ? BigDecimal.ZERO : campaign.getInitialBalanceBTC();

        return initial.add(total);
    }
}

