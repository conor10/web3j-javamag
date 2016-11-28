package org.web3j.javamag;

import java.math.BigDecimal;

import org.web3j.abi.Transfer;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

/**
 * Send Ether from one account to another.
 */
public class TransferEther {

    public static void main(String[] args) throws Exception {
        Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/

        Credentials credentials = WalletUtils.loadCredentials(
                "my password", "/path/to/walletfile");
        TransactionReceipt transactionReceipt = Transfer.sendFundsAsync(
                web3, credentials, "0x...", BigDecimal.valueOf(0.2), Convert.Unit.ETHER).get();
        System.out.println("Funds transfer completed, transaction hash: "
                + transactionReceipt.getTransactionHash() +  ", block number: " +
                transactionReceipt.getBlockNumber());
    }
}
