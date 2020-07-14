package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

//bll
public class GetLuckyNodesMessage extends VMessage {
    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) {
    }

    public GetLuckyNodesMessage(@NonNull Wallet izVar) {
        super(izVar, "getluckynode");
    }
}