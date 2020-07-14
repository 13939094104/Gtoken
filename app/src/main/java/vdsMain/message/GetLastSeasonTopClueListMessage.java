package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

//blj
public class GetLastSeasonTopClueListMessage extends VMessage {
    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) {
    }

    public void onDecodeSerialData() {
    }

    public GetLastSeasonTopClueListMessage(@NonNull Wallet izVar) {
        super(izVar, "gettopcllist");
    }
}
