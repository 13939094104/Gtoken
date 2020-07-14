package vdsMain.db;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

/* renamed from: ki */
public abstract class WalletDB extends DataBaseHelper {

    /* renamed from: f */
    protected Wallet wallet;

    public WalletDB(@NonNull Wallet wallet, @NonNull String prefix, @NonNull String name, int version) {
        super(wallet.getContext(), prefix, name, version);
        this.wallet = wallet;
    }

    /* access modifiers changed from: protected */
    /* renamed from: r */
    public Wallet getWallet() {
        return this.wallet;
    }
}
