package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

//bmp
public class VerackMessage extends VMessage {
    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) {
    }

    public VerackMessage(@NonNull Wallet izVar) {
        super(izVar, "verack", 209, 0);
        izVar.getChainParams().getClass();
    }
}