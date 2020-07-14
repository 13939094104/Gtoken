package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.QueneItem;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//bmt
public class VibWaitQueneMessage extends VMessage {

    /* renamed from: a */
    private List<QueneItem> f11941a = new ArrayList();

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) {
    }

    public VibWaitQueneMessage(@NonNull Wallet izVar) {
        super(izVar, "vibquene");
    }

    public void onDecodeSerialData() throws IOException {
        readObjectList(this.f11941a, QueneItem.class);
    }

    /* renamed from: a */
    public List<QueneItem> mo42606a() {
        return this.f11941a;
    }
}
