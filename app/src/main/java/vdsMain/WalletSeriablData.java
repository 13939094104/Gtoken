package vdsMain;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

public abstract class WalletSeriablData extends ParamedSeriableData {

    //f13033l
    //910 f13169l
    protected Wallet wallet = null;

    public WalletSeriablData(@NonNull Wallet wallet) {
        super(wallet.getChainParams());
        this.wallet = wallet;
    }

    public WalletSeriablData(WalletSeriablData walletSeriablData) {
        super((ParamedSeriableData) walletSeriablData);
        this.wallet = walletSeriablData.wallet;
    }

    //mo44133R
    public Wallet getSelfWallet() {
        return this.wallet;
    }
}