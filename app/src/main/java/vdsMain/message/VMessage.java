package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//bmn
//910 bnc
public abstract class VMessage extends Message {

    /* renamed from: j */
    protected int f11928j = 1;

    public void onDecodeSerialData() throws IOException {
    }

    public VMessage(@NonNull Wallet wallet, String str) {
        super(wallet, str);
        wallet.getChainParams().getClass();
        mo42592a(209, 0);
    }

    public VMessage(@NonNull Wallet izVar, String str, int i, int i2) {
        super(izVar, str);
        mo42593a(izVar, i, i2);
    }

    /* renamed from: a */
    public void mo42592a(int i, int i2) {
        mo42593a(this.wallet, i, i2);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42593a(Wallet wallet, int i, int i2) {
        if (i == 0) {
            i = wallet.getChainParams().protocalVersion;
        }
        this.mProtocalVersion = i | i2;
    }

    /* renamed from: n */
    public int mo42594n() {
        return this.f11928j;
    }
}
