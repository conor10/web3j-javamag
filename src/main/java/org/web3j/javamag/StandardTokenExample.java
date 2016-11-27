package org.web3j.javamag;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.javamag.generated.HumanStandardToken;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

/**
 * Demonstrate the Standard Token Solidity smart contract.
 *
 * Simplified version of <a href="https://github.com/web3j/web3j/blob/master/src/integration-test/java/org/web3j/protocol/scenarios/HumanStandardTokenGeneratedIT.java">
 * HumanStandardTokenGeneratedIT</a>.
 */
public class StandardTokenExample {

    private static final BigInteger GAS_PRICE = BigInteger.valueOf(50_000_000_000L);
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(2_000_000);

    public static void main(String[] args) throws Exception {
        Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/

        Credentials alice = WalletUtils.loadCredentials(
                "alice password", "/path/to/walletfile");

        Credentials bob = WalletUtils.loadCredentials(
                "bob password", "/path/to/walletfile");

        // deploy our smart contract
        HumanStandardToken contract = HumanStandardToken.deploy(
                web3, alice, GAS_PRICE, GAS_LIMIT,
                BigInteger.ZERO,
                new Uint256(BigInteger.valueOf(1000000)), new Utf8String("web3j tokens"),
                new Uint8(BigInteger.TEN), new Utf8String("w3j$")).get();

        // print the total supply issued
        Uint256 totalSupply = contract.totalSupply().get();
        System.out.println("Token supply issued: " + totalSupply.getValue());

        // check our token balance
        Uint256 balance = contract.balanceOf(new Address(alice.getAddress())).get();
        System.out.println("Your current balance is: w3j$" + balance.getValue());

        // transfer 100 tokens to bob
        TransactionReceipt transferReceipt = contract.transfer(
                new Address(bob.getAddress()), new Uint256(BigInteger.valueOf(100))).get();

        // set an allowance of 10,000 for bob
        TransactionReceipt approveReceipt = contract.approve(new Address(bob.getAddress()),
                new Uint256(BigInteger.valueOf(10_000))).get();

        // perform a transfer as Bob
        // Bob requires his own contract instance
        HumanStandardToken bobsContract = HumanStandardToken.load(
                contract.getContractAddress(), web3, bob, GAS_PRICE, GAS_LIMIT);

        TransactionReceipt bobTransferReceipt = bobsContract.transferFrom(
                new Address(alice.getAddress()),
                new Address(bob.getAddress()),
                new Uint256(BigInteger.valueOf(25))).get();
    }
}
