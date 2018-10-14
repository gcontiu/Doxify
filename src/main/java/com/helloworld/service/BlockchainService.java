package com.helloworld.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

/**
 * Created by gelu on 10/14/18.
 */
public class BlockchainService {

    public static final Logger log = LoggerFactory.getLogger(BlockchainService.class);

    public void contactBlockchain() throws IOException, CipherException, TransactionException, InterruptedException, ExecutionException {

        Web3j web3j = Web3j.build(new HttpService());

        log.info("Conncted to Ethereum client version: " + web3j.web3ClientVersion().send().getWeb3ClientVersion());

        Credentials credentials = WalletUtils.loadCredentials("supersafepassword", "chaindata\\keystore\\UTC--2018--<rest-of-the-filename>");

        log.info("Credentials loaded");

        log.info("Sending ether");

        TransactionReceipt transferReceipt = Transfer.sendFunds(web3j, credentials, "0x----",
                BigDecimal.valueOf(100), Convert.Unit.ETHER).sendAsync().get();

        log.info("Transaction complete: " + transferReceipt.getTransactionHash());


    }
}
