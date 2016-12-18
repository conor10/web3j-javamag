package org.web3j.javamag;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.javamag.generated.Greeter;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * Demonstrate the Greeter Solidity smart contract.
 */
public class HelloWorld {

    private static final BigInteger GAS_PRICE = BigInteger.valueOf(20_000_000_000L);
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);

    public static void main(String[] args) throws Exception {
        Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/

        Credentials credentials = WalletUtils.loadCredentials(
                "my password", "/path/to/walletfile");

        Greeter contract = Greeter.deploy(
                web3, credentials, GAS_PRICE, GAS_LIMIT, BigInteger.ZERO,
                new Utf8String("Hello blockchain world!"))
                .get();

        Utf8String greeting = contract.greet().get();
        System.out.println(greeting.getValue());
    }
}
