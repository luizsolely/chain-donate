package com.chaindonate.api.controller;

import com.chaindonate.api.dto.SimpleDonationDTO;
import com.chaindonate.api.entity.Donation;
import com.chaindonate.api.mapper.DonationMapper;
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
    private final DonationMapper donationMapper;

    @GetMapping
    public List<SimpleDonationDTO> getDonationsByCampaign(@PathVariable Long id) {
        List<Donation> donations = donationService.findByCampaignId(id);
        return donations.stream()
                .map(donationMapper::toSimpleDto)
                .collect(Collectors.toList());
    }

}
