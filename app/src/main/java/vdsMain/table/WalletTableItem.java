package vdsMain.table;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

public abstract class WalletTableItem extends AbstractTableItem {

    //f13165s
    protected Wallet wallet;

    public WalletTableItem(@NonNull WalletTable walletTable) {
        super(walletTable.getContext(), walletTable);
        this.wallet = walletTable.getWallet();
    }

    /* access modifiers changed from: protected */
    /* mo44388ae */
    public Wallet getWallet() {
        return this.wallet;
    }
}
