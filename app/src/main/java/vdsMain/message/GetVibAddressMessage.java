package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

//blr
public class GetVibAddressMessage extends VMessage {
    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) {
    }

    public GetVibAddressMessage(@NonNull Wallet izVar) {
        super(izVar, "getvibaddr");
    }
}
