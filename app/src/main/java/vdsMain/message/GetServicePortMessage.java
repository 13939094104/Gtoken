package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

//blm
public class GetServicePortMessage extends VMessage {
    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) {
    }

    public GetServicePortMessage(@NonNull Wallet izVar) {
        super(izVar, "getservport");
    }
}
