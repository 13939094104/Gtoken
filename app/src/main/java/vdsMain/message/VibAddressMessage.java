package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

//bmr
public class VibAddressMessage extends VMessage {
    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) {
    }

    public VibAddressMessage(@NonNull Wallet izVar) {
        super(izVar, "getvibaddr");
    }
}
