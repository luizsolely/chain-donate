package com.chaindonate.api.service;

import com.chaindonate.api.client.MempoolSpaceClient;
import com.chaindonate.api.dto.CampaignRequestDTO;
import com.chaindonate.api.dto.CampaignResponseDTO;
import com.chaindonate.api.entity.Campaign;
import com.chaindonate.api.entity.User;
import com.chaindonate.api.exception.BtcAddressAlreadyInUseException;
import com.chaindonate.api.exception.CampaignNotFoundException;
import com.chaindonate.api.mapper.CampaignMapper;
import com.chaindonate.api.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final MempoolSpaceClient  mempoolSpaceClient;
    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;
    private final UserService userService;

    public Campaign findById(Long id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignNotFoundException("Campaign not found with id: " + id));
    }

    public CampaignResponseDTO createCampaign(CampaignRequestDTO dto, String userEmail) {
        if(campaignRepository.findByBtcAddress(dto.btcAddress()).isPresent()) {
            throw new BtcAddressAlreadyInUseException("BTC address already in use: " +  dto.btcAddress());
        }

        User user = userService.findByEmail(userEmail);
        Campaign campaign = campaignMapper.toEntity(dto);

        campaign.setUser(user);
        campaign.setCreatedAt(LocalDateTime.now());

        // Consulta o saldo atual do endereço
        BigDecimal initialBalance = mempoolSpaceClient.getAddressBalance(dto.btcAddress());
        campaign.setInitialBalanceBTC(initialBalance);

        Campaign saved = campaignRepository.save(campaign);
        return campaignMapper.toDto(saved);
    }

    public List<CampaignResponseDTO> getAllCampaigns() {
        return campaignRepository.findAll().stream()
                .map(campaignMapper::toDto)
                .collect(Collectors.toList());
    }

    public CampaignResponseDTO getCampaignByBtcAddress(String btcAddress) {
        return campaignRepository.findByBtcAddress(btcAddress)
                .map(campaignMapper::toDto)
                .orElseThrow(() -> new CampaignNotFoundException("Campaign not found with address: " + btcAddress));
    }

}
