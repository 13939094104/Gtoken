package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.ad.CAd;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//bkk
public class AdListMessage extends VMessage {

    /* renamed from: a */
    List<CAd> f11835a = new ArrayList();

    public AdListMessage(@NonNull Wallet izVar) {
        super(izVar, "ad");
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeObjectList(this.f11835a);
    }

    public void onDecodeSerialData() {
        int b = readVariableInt().getIntValue();
        for (int i = 0; i < b; i++) {
            CAd biy = new CAd();
            biy.decodeSerialStream(getTempInput());
            this.f11835a.add(biy);
        }
    }

    /* renamed from: a */
    public List<CAd> mo42549a() {
        return this.f11835a;
    }

    /* renamed from: a */
    public void mo42550a(List<CAd> list) {
        this.f11835a = list;
    }
}
