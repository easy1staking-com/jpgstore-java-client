package com.easy1staking.jpgstore.jpgstorejavaclient.service;

import com.bloxbean.cardano.aiken.AikenTransactionEvaluator;
import com.bloxbean.cardano.client.account.Account;
import com.bloxbean.cardano.client.api.model.Amount;
import com.bloxbean.cardano.client.api.model.Result;
import com.bloxbean.cardano.client.api.model.Utxo;
import com.bloxbean.cardano.client.backend.blockfrost.service.BFBackendService;
import com.bloxbean.cardano.client.function.helper.SignerProviders;
import com.bloxbean.cardano.client.plutus.spec.ConstrPlutusData;
import com.bloxbean.cardano.client.plutus.spec.PlutusData;
import com.bloxbean.cardano.client.quicktx.QuickTxBuilder;
import com.bloxbean.cardano.client.quicktx.ScriptTx;
import com.easy1staking.jpgstore.jpgstorejavaclient.model.ListingDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.net.URISyntaxException;

@Component
public class JpgstorePurchaseService {

    @org.springframework.beans.factory.annotation.Value("${ogmios.url}")
    private  String ogmiosUrl;

    @Autowired
    private  Account account;

    @Autowired
    private  BFBackendService backendService;

    public void purchaseNft(Utxo utxo, PlutusData plutusData,
                            ListingDetails listingDetails) throws URISyntaxException {


        ScriptTx scriptTx = new ScriptTx()
                .collectFrom(utxo, ConstrPlutusData.of(1), plutusData)
                // Jpg Fees - PKH 70e60f3b5ea7153e0acc7a803e4401d44b8ed1bae1c7baaad1a62a72
                .payToAddress(listingDetails.jpgStoreFees().beneficiary(), Amount.lovelace(BigInteger.valueOf(listingDetails.jpgStoreFees().lovelaces())))
                // Royalties address - PKH f44f8751f03d767ba51c4fe988ed289b7ced4c7e832223d44b31bcce
                .payToAddress(listingDetails.royalties().get().beneficiary(), Amount.lovelace(BigInteger.valueOf(listingDetails.royalties().get().lovelaces())))
                // Seller adress - PKH 9f5fa54a4421797260ed5323df3772c048bc7aba6e241b2e05a43319
                .payToAddress(listingDetails.seller().beneficiary(), Amount.lovelace(BigInteger.valueOf(listingDetails.seller().lovelaces())))
                .readFrom("9a32459bd4ef6bbafdeb8cf3b909d0e3e2ec806e4cc6268529280b0fc1d06f5b", 0)
                .withChangeAddress(account.baseAddress());


        QuickTxBuilder quickTxBuilder = new QuickTxBuilder(backendService);

        Result<String> allGood1 = quickTxBuilder.compose(scriptTx)
                .feePayer(account.baseAddress())
                .withSigner(SignerProviders.signerFrom(account))
                .withTxEvaluator(new AikenTransactionEvaluator(backendService))
                .completeAndWait();

        System.out.println(allGood1.getResponse());

    }


}
