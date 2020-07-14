package vdsMain.transaction;

import androidx.annotation.NonNull;
import vdsMain.BLOCK_CHAIN_TYPE;
import vdsMain.wallet.Wallet;

public class VOfflineTransaction extends OfflineTransaction {
    public VOfflineTransaction(@NonNull Wallet izVar) {
        super(izVar);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo44665a() {
        if (this.f13341d != BLOCK_CHAIN_TYPE.VCASH) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalidate Offline tx type: ");
            sb.append(this.f13341d);
            throw new IllegalArgumentException(sb.toString());
        }
    }
}