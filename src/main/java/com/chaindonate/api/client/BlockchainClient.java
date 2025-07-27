package com.chaindonate.api.client;

import com.chaindonate.api.entity.Campaign;
import com.chaindonate.api.entity.Donation;

import java.util.List;

public interface BlockchainClient {
    List<Donation> getDonationsForAddress(String btcAddress, Campaign campaign);
}
