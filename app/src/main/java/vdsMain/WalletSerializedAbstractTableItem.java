package vdsMain;

import androidx.annotation.NonNull;
import vdsMain.table.AbstractTable;
import vdsMain.wallet.Wallet;

public abstract class WalletSerializedAbstractTableItem extends SerializedAbstractTableItem {

    //f13335X
    protected Wallet wallet;

    public WalletSerializedAbstractTableItem(@NonNull Wallet izVar) {
        this.wallet = izVar;
    }

    public WalletSerializedAbstractTableItem(@NonNull Wallet wallet, @NonNull AbstractTable fxVar) {
        super(wallet.getContext(), fxVar);
        this.wallet = wallet;
    }

    public WalletSerializedAbstractTableItem(WalletSerializedAbstractTableItem lrVar) {
        super(lrVar);
        if (lrVar != null) {
            this.wallet = lrVar.wallet;
        }
    }

    /* renamed from: R */
    public Wallet mo44660R() {
        return this.wallet;
    }

    //mo44661a
    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}
