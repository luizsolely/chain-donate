package com.chaindonate.api.service;

import com.chaindonate.api.client.BlockchainClient;
import com.chaindonate.api.entity.Campaign;
import com.chaindonate.api.entity.Donation;
import com.chaindonate.api.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DonationService {

    private static final Logger log = LoggerFactory.getLogger(DonationService.class);
    private final DonationRepository donationRepository;
    private final CampaignService campaignService;
    private final BlockchainClient blockchainClient;

    public List<Donation> getOrSyncDonationsByCampaignId(Long campaignId) {
        Campaign campaign = campaignService.findById(campaignId);

        try {
            List<Donation> freshDonations = blockchainClient.getDonationsForAddress(campaign.getBtcAddress(), campaign);

            if (!freshDonations.isEmpty()) {
                return donationRepository.saveAll(freshDonations);
            }
        } catch (Exception e) {
            log.error("Erro ao buscar doações da blockchain para campanha {}: {}", campaignId, e.getMessage(), e);
        }

        return donationRepository.findByCampaignId(campaignId);
    }


    public List<Donation> forceSyncDonationsByCampaignId(Long campaignId) {
        Campaign campaign = campaignService.findById(campaignId);

        donationRepository.deleteByCampaignId(campaignId);

        List<Donation> newDonations = blockchainClient.getDonationsForAddress(campaign.getBtcAddress(), campaign);
        return donationRepository.saveAll(newDonations);
    }
}
