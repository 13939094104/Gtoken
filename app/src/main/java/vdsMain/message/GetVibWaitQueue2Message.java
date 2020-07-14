package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

//blu
public class GetVibWaitQueue2Message extends VMessage {
    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) {
    }

    public GetVibWaitQueue2Message(@NonNull Wallet izVar) {
        super(izVar, "getvibqueue");
    }
}
