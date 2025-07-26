package com.chaindonate.api.controller;

import com.chaindonate.api.auth.CustomUserDetails;
import com.chaindonate.api.dto.CampaignRequestDTO;
import com.chaindonate.api.dto.CampaignResponseDTO;
import com.chaindonate.api.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    @GetMapping
    public List<CampaignResponseDTO> getAllCampaigns() {
        return campaignService.getAllCampaigns();
    }

    @PostMapping
    public ResponseEntity<CampaignResponseDTO> createCampaign(
            @RequestBody CampaignRequestDTO dto,
            Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String userEmail = userDetails.getUserEntity().getEmail();

        CampaignResponseDTO response = campaignService.createCampaign(dto, userEmail);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{btcAddress}")
    public CampaignResponseDTO getCampaignByBtcAddress(@PathVariable String btcAddress) {
        return campaignService.getCampaignByBtcAddress(btcAddress);
    }
}
