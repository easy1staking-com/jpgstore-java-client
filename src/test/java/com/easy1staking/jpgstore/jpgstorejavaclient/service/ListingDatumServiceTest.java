package com.easy1staking.jpgstore.jpgstorejavaclient.service;

import com.bloxbean.cardano.client.plutus.spec.PlutusData;
import com.easy1staking.jpgstore.jpgstorejavaclient.config.BlockfrostConfig;
import com.easy1staking.jpgstore.jpgstorejavaclient.config.JacksonConfig;
import io.blockfrost.sdk.api.model.AddressUtxo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest(classes = {BlockfrostConfig.class, JacksonConfig.class, ListingService.class, ListingDatumService.class})
class ListingDatumServiceTest {

    @Autowired
    ListingService listingService;
    @Autowired
    ListingDatumService listingDatumService;

    @Test
    void test() {
        Optional<AddressUtxo> addressUtxo = listingService
                .findListing("a5bb0e5bb275a573d744a021f9b3bff73595468e002755b447e01559484f534b594361736847726162303030333838303130");

        Optional<PlutusData> plutusData = addressUtxo.flatMap(utxo -> listingDatumService.findPlutusData(utxo.getTxHash(), utxo.getDataHash()));

        plutusData
                .ifPresent(datum -> System.out.println(datum.serializeToHex()));

    }

    @Test
    void extractListingDetails() {
        // NFT (unit/asset_id): a5bb0e5bb275a573d744a021f9b3bff73595468e002755b447e01559484f534b594361736847726162303030333838303130
        // jpg: addr1qyfjhayd6fq2xnu8wyyz705qewxzfrgp0v4r70rpujw2x6wta8cxth5k90299dhqnaum8wh5h83m64l4ydrxehfdqnasxsjplq
        // Seller: addr1qyfjhayd6fq2xnu8wyyz705qewxzfrgp0v4r70rpujw2x6wta8cxth5k90299dhqnaum8wh5h83m64l4ydrxehfdqnasxsjplq
        // Seller: addr1qyfjhayd6fq2xnu8wyyz705qewxzfrgp0v4r70rpujw2x6wta8cxth5k90299dhqnaum8wh5h83m64l4ydrxehfdqnasxsjplq


        String datum = "d8799f581c132bf48dd240a34f8771082f3e80cb8c248d017b2a3f3c61e49ca3699fd8799fd8799fd8799f581c70e60f3b5ea7153e0acc7a803e4401d44b8ed1bae1c7baaad1a62a72ffd8799fd8799fd8799f581c1e78aae7c90cc36d624f7b3bb6d86b52696dc84e490f343eba89005fffffffffa140d8799f00a1401a000f4240ffffd8799fd8799fd8799f581cf44f8751f03d767ba51c4fe988ed289b7ced4c7e832223d44b31bcceffd8799fd8799fd8799f581c2341d750452778e9a0962600af8e05eef6697c83df9f492103bc8b53ffffffffa140d8799f00a1401a000f4240ffffd8799fd8799fd8799f581c132bf48dd240a34f8771082f3e80cb8c248d017b2a3f3c61e49ca369ffd8799fd8799fd8799f581ccbe9f065de962bd452b6e09f79b3baf4b9e3bd57f523466cdd2d04fbffffffffa140d8799f00a1401a00b71b00ffffffff";

        listingDatumService
                .deserializeDatum(datum)
                .flatMap(jsonDatum -> listingDatumService.extractListingDetails(jsonDatum))
                .ifPresent(System.out::println);

    }


}