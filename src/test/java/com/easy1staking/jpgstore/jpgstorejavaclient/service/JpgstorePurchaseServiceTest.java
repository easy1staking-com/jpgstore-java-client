package com.easy1staking.jpgstore.jpgstorejavaclient.service;

import com.bloxbean.cardano.client.api.exception.ApiException;
import com.bloxbean.cardano.client.api.model.Result;
import com.bloxbean.cardano.client.api.model.Utxo;
import com.bloxbean.cardano.client.backend.blockfrost.service.BFBackendService;
import com.bloxbean.cardano.client.plutus.spec.PlutusData;
import com.easy1staking.jpgstore.jpgstorejavaclient.config.AccountConfig;
import com.easy1staking.jpgstore.jpgstorejavaclient.config.BlockfrostConfig;
import com.easy1staking.jpgstore.jpgstorejavaclient.config.JacksonConfig;
import com.easy1staking.jpgstore.jpgstorejavaclient.model.Constants;
import com.easy1staking.jpgstore.jpgstorejavaclient.model.ListingDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {AccountConfig.class, BlockfrostConfig.class, JacksonConfig.class, JpgstorePurchaseService.class, ListingDatumService.class})
class JpgstorePurchaseServiceTest {

    @Autowired
    private JpgstorePurchaseService jpgstorePurchaseService;

    @Autowired
    private BFBackendService bfBackendService;

    @Autowired
    private ListingDatumService listingDatumService;


    /**
     * Fully working code to puchase an NFT from jpg.store
     * @throws ApiException
     */
    @Test
    public void purchaseNft() throws ApiException {

        Result<List<Utxo>> utxos = bfBackendService.getUtxoService().getUtxos(Constants.JPG_CONTRACT_ADDRESS,
                // unit or asset_id (hex encode policy id + asset name)
                "a5bb0e5bb275a573d744a021f9b3bff73595468e002755b447e01559484f534b594361736847726162303030333535363030",
                1,
                1);

        utxos.getValue().stream().findFirst()
                .ifPresent(utxo -> {
                    System.out.println(utxo);

                    Optional<PlutusData> plutusData = listingDatumService.findPlutusData(utxo.getTxHash(), utxo.getDataHash());

                    Optional<ListingDetails> listingDetails = plutusData
                            .flatMap(data -> listingDatumService.deserializeDatum(data))
                            .flatMap(datum -> listingDatumService.extractListingDetails(datum));

                    try {
                        jpgstorePurchaseService.purchaseNft(utxo, plutusData.get(), listingDetails.get());
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });


        assertTrue(true);

    }

}