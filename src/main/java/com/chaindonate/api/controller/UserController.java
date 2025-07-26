package com.chaindonate.api.controller;

import com.chaindonate.api.dto.SimpleCampaignDTO;
import com.chaindonate.api.dto.UserResponseDTO;
import com.chaindonate.api.entity.User;
import com.chaindonate.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserResponseDTO getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        List<SimpleCampaignDTO> campaignDTOs = user.getCampaigns().stream()
                .map(c -> new SimpleCampaignDTO(c.getId(), c.getTitle(), c.getBtcAddress()))
                .toList();

        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                campaignDTOs
        );
    }

    @GetMapping("/me/campaigns")
    public ResponseEntity<List<SimpleCampaignDTO>> getMyCampaigns(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        List<SimpleCampaignDTO> dtos = user.getCampaigns().stream()
                .map(c -> new SimpleCampaignDTO(c.getId(), c.getTitle(), c.getBtcAddress()))
                .toList();

        return ResponseEntity.ok(dtos);
    }

}
