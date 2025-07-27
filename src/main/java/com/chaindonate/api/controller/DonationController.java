package com.chaindonate.api.controller;

import com.chaindonate.api.dto.SimpleDonationDTO;
import com.chaindonate.api.entity.Donation;
import com.chaindonate.api.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/campaigns/{id}/donations")
public class DonationController {

    private final DonationService donationService;

    @GetMapping
    public List<SimpleDonationDTO> getDonationsByCampaign(@PathVariable Long id) {
        List<Donation> donations = donationService.getOrSyncDonationsByCampaignId(id);

        return donations.stream().map(donation -> {
            SimpleDonationDTO dto = new SimpleDonationDTO(donation.getId(),
                    donation.getAmountBTC(),
                    true,
                    donation.getTimestamp());
            return dto;
        }).collect(Collectors.toList());
    }
}
