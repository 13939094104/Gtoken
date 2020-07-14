package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

//blk
public class GetLastWeekClueRankingMessage extends VMessage {
    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) {
    }

    public GetLastWeekClueRankingMessage(@NonNull Wallet izVar) {
        super(izVar, "getclrk");
    }
}