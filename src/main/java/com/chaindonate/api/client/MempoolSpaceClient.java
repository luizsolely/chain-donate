package com.chaindonate.api.client;

import com.chaindonate.api.entity.Campaign;
import com.chaindonate.api.entity.Donation;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class MempoolSpaceClient implements BlockchainClient {

    private final WebClient webClient = WebClient.create("https://mempool.space/api");

    public BigDecimal getAddressBalance(String btcAddress) {
        try {
            Map<String, Object> response = webClient
                    .get()
                    .uri("/address/{address}", btcAddress)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            if (response == null) {
                throw new RuntimeException("Empty response from blockchain API for address balance");
            }

            Object chainStatsObj = response.get("chain_stats");
            if (!(chainStatsObj instanceof Map)) {
                throw new RuntimeException("Unexpected response structure: 'chain_stats' missing or invalid");
            }

            Map<String, Object> chainStats = (Map<String, Object>) chainStatsObj;
            Long funded = extractLong(chainStats.get("funded_txo_sum"));
            Long spent = extractLong(chainStats.get("spent_txo_sum"));

            long balanceSats = (funded != null ? funded : 0L) - (spent != null ? spent : 0L);

            return BigDecimal.valueOf(balanceSats).movePointLeft(8); // sats → BTC

        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error fetching address balance from blockchain API: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error fetching address balance", e);
        }
    }

    @Override
    public List<Donation> getDonationsForAddress(String btcAddress, Campaign campaign) {
        List<Map<String, Object>> txs = webClient
                .get()
                .uri("/address/{address}/txs", btcAddress)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .block();

        List<Donation> donations = new ArrayList<>();

        for (Map<String, Object> tx : txs) {
            String txid = (String) tx.get("txid");
            Map<String, Object> status = (Map<String, Object>) tx.get("status");
            Number blockTime = (Number) status.get("block_time");

            List<Map<String, Object>> voutList = (List<Map<String, Object>>) tx.get("vout");

            for (Map<String, Object> output : voutList) {
                String outputAddress = (String) output.get("scriptpubkey_address");

                if (btcAddress.equals(outputAddress)) {
                    Number valueSats = (Number) output.get("value");

                    Donation donation = new Donation();
                    donation.setTxHash(txid);
                    donation.setAmountBTC(BigDecimal.valueOf(valueSats.longValue()).movePointLeft(8));
                    donation.setConfirmed(true);
                    donation.setTimestamp(LocalDateTime.ofInstant(Instant.ofEpochSecond(blockTime.longValue()), ZoneOffset.UTC));
                    donation.setCampaign(campaign);

                    donations.add(donation);
                }
            }
        }

        return donations;
    }

    private Long extractLong(Object obj) {
        if (obj instanceof Number num) {
            return num.longValue();
        }
        return null;
    }

    private long extractBlockTime(Object statusObj) {
        if (statusObj instanceof Map) {
            Object blockTimeObj = ((Map<?, ?>) statusObj).get("block_time");
            Long blockTime = extractLong(blockTimeObj);
            if (blockTime != null) {
                return blockTime;
            }
        }
        return Instant.now().getEpochSecond();
    }
}
