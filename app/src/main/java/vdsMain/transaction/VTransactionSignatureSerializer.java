package vdsMain.transaction;

import androidx.annotation.NonNull;
import bitcoin.script.CScript;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//bpu
public class VTransactionSignatureSerializer extends CTransactionSignatureSerializer {

    /* renamed from: h */
    public boolean f12045h;

    /* access modifiers changed from: protected */
    /* renamed from: e */
    public void mo42758e(CHashWriter kVar) {
    }

    public VTransactionSignatureSerializer(@NonNull Wallet izVar, TransactionInteface diVar, CScript cScript, int i, int i2, boolean z) {
        super(izVar, diVar, cScript, i, i2);
        this.f12045h = z;
        boolean z2 = true;
        this.f12279d = (i2 & 128) != 0;
        int i3 = i2 & 31;
        this.f12280e = i3 == 3;
        if (i3 != 2) {
            z2 = false;
        }
        this.f12281f = z2;
    }

    /* renamed from: b */
    public void mo42757b(CHashWriter kVar) throws IOException {
        kVar.writeUInt32T(this.f12276a.getVersion());
        mo43146c(kVar);
        mo43147d(kVar);
        kVar.writeUInt32T(this.f12276a.getLockTime());
        mo42758e(kVar);
    }
}
