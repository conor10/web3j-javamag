package org.web3j.javamag;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

/**
 * Obtain version of Ethereum client being run.
 */
public class ClientVersion {

    public static void main(String[] args) throws Exception {
        Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
        Web3ClientVersion clientVersion = web3.web3ClientVersion().sendAsync().get();

        if (!clientVersion.hasError()) {
            System.out.println("Client is running version: " + clientVersion.getWeb3ClientVersion());
        }
    }
}
