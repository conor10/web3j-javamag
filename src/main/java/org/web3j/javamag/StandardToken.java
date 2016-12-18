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
 *
 * You'll require two different Ethereum wallet files to run this code.
 */
public class StandardToken {

    // https://www.reddit.com/r/ethereum/comments/5g8ia6/attention_miners_we_recommend_raising_gas_limit/
    private static final BigInteger GAS_PRICE = BigInteger.valueOf(20_000_000_000L);
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);

    public static void main(String[] args) throws Exception {
        Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/

        // user 1
        Credentials alice = WalletUtils.loadCredentials(
                "alice password", "/path/to/walletfile");

        // user 2
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
        System.out.println("Token supply issued: w3j$ " + totalSupply.getValue());

        // check our token balance
        System.out.println("Alice's current balance is: w3j$ " + getBalance(contract, alice).getValue());

        // transfer 100 tokens to bob
        TransactionReceipt transferReceipt = contract.transfer(
                new Address(bob.getAddress()), new Uint256(BigInteger.valueOf(100))).get();
        System.out.println("Transfer completed with tx hash: " + transferReceipt.getTransactionHash());

        // set an allowance of 10,000 for bob
        TransactionReceipt approveReceipt = contract.approve(new Address(bob.getAddress()),
                new Uint256(BigInteger.valueOf(10_000))).get();
        System.out.println("Approval request processed with tx hash: " +
                approveReceipt.getTransactionHash());

        // perform a transfer as Bob
        // Bob requires his own contract instance which references the existing deployed contract
        HumanStandardToken bobsContract = HumanStandardToken.load(
                contract.getContractAddress(), web3, bob, GAS_PRICE, GAS_LIMIT);

        TransactionReceipt bobTransferReceipt = bobsContract.transferFrom(
                new Address(alice.getAddress()),
                new Address(bob.getAddress()),
                new Uint256(BigInteger.valueOf(25))).get();
        System.out.println("Transfer completed with tx hash: " +
                bobTransferReceipt.getTransactionHash());

        System.out.println("Alice's balance is: w3j$ " + getBalance(contract, alice).getValue());
        System.out.println("Bob's balance is: w3j$ " + getBalance(contract, bob).getValue());
    }

    private static Uint256 getBalance(
            HumanStandardToken contract, Credentials credentials) throws Exception {
        return contract.balanceOf(new Address(credentials.getAddress())).get();
    }
}
