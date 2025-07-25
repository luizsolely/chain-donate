package com.chaindonate.api.repository;

import com.chaindonate.api.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByUserId(Long userId);
    Optional<Campaign> findByBtcAddress(String btcAddress);
}
