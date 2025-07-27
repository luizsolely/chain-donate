package com.chaindonate.api.service;

import com.chaindonate.api.client.BlockchainClient;
import com.chaindonate.api.entity.Campaign;
import com.chaindonate.api.entity.Donation;
import com.chaindonate.api.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository donationRepository;
    private final CampaignService campaignService;
    private final BlockchainClient blockchainClient;

    public List<Donation> getOrSyncDonationsByCampaignId(Long campaignId) {
        List<Donation> donations = donationRepository.findByCampaignId(campaignId);

        if (!donations.isEmpty()) {
            return donations;
        }

        Campaign campaign = campaignService.findById(campaignId);

        List<Donation> freshDonations = blockchainClient.getDonationsForAddress(campaign.getBtcAddress(), campaign);

        if (freshDonations.isEmpty()) {
            return freshDonations;
        }

        return donationRepository.saveAll(freshDonations);
    }

    public List<Donation> forceSyncDonationsByCampaignId(Long campaignId) {
        Campaign campaign = campaignService.findById(campaignId);

        donationRepository.deleteByCampaignId(campaignId);

        List<Donation> newDonations = blockchainClient.getDonationsForAddress(campaign.getBtcAddress(), campaign);
        return donationRepository.saveAll(newDonations);
    }
}
