package com.chaindonate.api.service;

import com.chaindonate.api.entity.Campaign;
import com.chaindonate.api.entity.Donation;
import com.chaindonate.api.repository.DonationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonationService {

    private final DonationRepository donationRepository;

    public DonationService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    public List<Donation> findByCampaignId(Long campaignId) {
        return donationRepository.findByCampaignId(campaignId);
    }

}
