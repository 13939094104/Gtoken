package vdsMain;

import androidx.annotation.NonNull;

import generic.utils.AddressUtils;
import vdsMain.wallet.Wallet;

public class SubAddressInfo {

    //f13121a
    public long sumBalance;

    //f13122b
    public long availBalance;

    //f13123c
    public Wallet wallet;

    public SubAddressInfo(@NonNull Wallet wallet) {
        this.wallet = wallet;
    }

    //mo44261a
    public String getAddressString(CTxDestination cTxDestination) {
        return AddressUtils.getAddressString(cTxDestination, this.wallet.getChainParams());
    }

    //mo44260a
    public BLOCK_CHAIN_TYPE getBlockChainType() {
        return this.wallet.getBlockChainType();
    }
}