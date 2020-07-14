package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.QueneItem;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//bmu
public class VibWaitQueue2Message extends VMessage {

    /* renamed from: a */
    private List<QueneItem> f11942a = new ArrayList();

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) {
    }

    public VibWaitQueue2Message(@NonNull Wallet izVar) {
        super(izVar, "getvibqueue");
    }

    public void onDecodeSerialData() throws IOException {
        readObjectList(this.f11942a, QueneItem.class);
    }

    /* renamed from: a */
    public List<QueneItem> mo42607a() {
        return this.f11942a;
    }
}
