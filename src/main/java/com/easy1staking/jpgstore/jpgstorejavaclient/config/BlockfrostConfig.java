package com.easy1staking.jpgstore.jpgstorejavaclient.config;

import com.bloxbean.cardano.client.backend.blockfrost.common.Constants;
import com.bloxbean.cardano.client.backend.blockfrost.service.BFBackendService;
import io.blockfrost.sdk.api.AddressService;
import io.blockfrost.sdk.api.MetadataService;
import io.blockfrost.sdk.api.TransactionService;
import io.blockfrost.sdk.impl.AddressServiceImpl;
import io.blockfrost.sdk.impl.MetadataServiceImpl;
import io.blockfrost.sdk.impl.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlockfrostConfig {

    @Value("${blockfrost.url}")
    private String blockfrostUrl;

    @Value("${blockfrost.key}")
    private String blockfrostKey;

    @Bean
    public AddressService addressService() {
        return new AddressServiceImpl(blockfrostUrl, "");
    }

    @Bean
    public TransactionService transactionService() {
        return new TransactionServiceImpl(blockfrostUrl, "");
    }

    @Bean
    public MetadataService metadataService() {
        return new MetadataServiceImpl(blockfrostUrl, "");
    }

    @Bean
    public BFBackendService bfBackendService() {
        return new BFBackendService(Constants.BLOCKFROST_MAINNET_URL, blockfrostKey);
    }

}
