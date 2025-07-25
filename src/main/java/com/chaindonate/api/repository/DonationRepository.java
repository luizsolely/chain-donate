package com.chaindonate.api.repository;

import com.chaindonate.api.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DonationRepository extends JpaRepository<Donation,Long> {
    List<Donation> findByCampaignId(Long campaignId);
    Optional<Donation> findByTxHashAndCampaignId(String txHash, Long campaignId);
}
