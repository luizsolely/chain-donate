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
    private final BlockchainClient blockchainClient;

    public List<Donation> findByCampaignId(Long campaignId) {
        return donationRepository.findByCampaignId(campaignId);
    }

    public void syncDonations(Campaign campaign) {
        List<Donation> blockchainDonations = blockchainClient.getDonationsForAddress(campaign.getBtcAddress(), campaign);

        for (Donation donation : blockchainDonations) {
            if (!donationRepository.existsByTxHash(donation.getTxHash())) {
                donationRepository.save(donation);
            }
        }
    }
}
