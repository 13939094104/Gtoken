package vdsMain.table;

import androidx.annotation.NonNull;
import vdsMain.db.WalletDB;
import vdsMain.wallet.Wallet;

/* renamed from: kj */
public abstract class WalletTable extends AbstractTable {

    //f13164b
    protected Wallet wallet;

    public WalletTable(@NonNull WalletDB walletDB) {
        super(walletDB.getContext().getApplicationContext(), walletDB);
        this.wallet = walletDB.getWallet();
    }


    public Wallet getWallet() {
        return this.wallet;
    }
}
