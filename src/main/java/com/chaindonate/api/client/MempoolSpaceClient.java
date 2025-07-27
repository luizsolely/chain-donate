package com.chaindonate.api.client;

import com.chaindonate.api.entity.Campaign;
import com.chaindonate.api.entity.Donation;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MempoolSpaceClient implements BlockchainClient {

    private final WebClient webClient = WebClient.create("https://mempool.space/api");

    public BigDecimal getAddressBalance(String btcAddress) {
        Map<String, Object> response = webClient
                .get()
                .uri("/address/{address}", btcAddress)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        if (response == null || !response.containsKey("chain_stats")) {
            throw new RuntimeException("Failed to fetch balance from blockchain");
        }

        Map<String, Object> chainStats = (Map<String, Object>) response.get("chain_stats");
        Long funded = ((Number) chainStats.get("funded_txo_sum")).longValue();
        Long spent = ((Number) chainStats.get("spent_txo_sum")).longValue();

        long balanceSats = funded - spent;

        return BigDecimal.valueOf(balanceSats).movePointLeft(8); // converte satoshi → BTC
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

        if (txs == null) return donations;

        for (Map<String, Object> tx : txs) {
            List<Map<String, Object>> outputs = (List<Map<String, Object>>) tx.get("vout");
            String txid = (String) tx.get("txid");
            Long timestamp = (Long) tx.get("status") != null
                    ? (Long) ((Map<String, Object>) tx.get("status")).get("block_time")
                    : Instant.now().getEpochSecond();

            for (Map<String, Object> output : outputs) {
                List<String> addresses = (List<String>) output.get("scriptpubkey_address") != null ?
                        List.of((String) output.get("scriptpubkey_address")) : List.of();

                if (addresses.contains(btcAddress)) {
                    Long valueSats = ((Number) output.get("value")).longValue();

                    Donation donation = new Donation();
                    donation.setTxHash(txid);
                    donation.setAmountBTC(BigDecimal.valueOf(valueSats).movePointLeft(8));
                    donation.setConfirmed(true);
                    donation.setTimestamp(LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneOffset.UTC));
                    donation.setCampaign(campaign);

                    donations.add(donation);
                }
            }
        }

        return donations;
    }
}
