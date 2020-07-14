package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

//blt
public class GetVibWaitQueneMessage extends VMessage {
    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) {
    }

    public GetVibWaitQueneMessage(@NonNull Wallet izVar) {
        super(izVar, "getvibquene");
    }
}
