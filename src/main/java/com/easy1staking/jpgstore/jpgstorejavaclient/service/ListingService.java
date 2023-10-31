package com.easy1staking.jpgstore.jpgstorejavaclient.service;

import com.easy1staking.jpgstore.jpgstorejavaclient.model.Constants;
import io.blockfrost.sdk.api.AddressService;
import io.blockfrost.sdk.api.exception.APIException;
import io.blockfrost.sdk.api.model.AddressUtxo;
import io.blockfrost.sdk.api.util.OrderEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class ListingService {

    private final AddressService addressService;

    /**
     * jpg.store seems to be using a combination of PlutusV1, PlutusV2 with Reference Scripts,
     * but does not make use of inline datum but only data hash in PlutusV1 style.
     *
     * The Plutus Data or Datum that needs to be used with the purchase script, seems to have been attached at NFT listing time.
     * In order to buy an NFT, given its asset_id aka unit (see below for definition), it is necessary to find
     * listing transaction, fetch the utxo (i.e. listing transaction), find the transaction and extract Plutus Data / Datum
     * from the transaction Metadata.
     *
     * As a result of reverse engineer the purchase action, it's necessary to find the listing transaction of the
     * NFT to purchase.
     *
     * @param unit unit or asset id, hex encode of policy id + asset name
     * @return
     */
    public Optional<AddressUtxo> findListing(String unit) {
        try {
            return addressService
                    .getAddressUtxosGivenAsset(Constants.JPG_CONTRACT_ADDRESS, unit, 1, 1, OrderEnum.asc)
                    .stream()
                    .findFirst();
        } catch (APIException e) {
            log.error("error while fetching listing details", e);
            return Optional.empty();
        }
    }

}
