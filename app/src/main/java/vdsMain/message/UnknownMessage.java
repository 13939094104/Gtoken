package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

//bml
public class UnknownMessage extends VMessage {
    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) {
    }

    public UnknownMessage(@NonNull Wallet izVar, String str) {
        super(izVar, str);
    }
}
