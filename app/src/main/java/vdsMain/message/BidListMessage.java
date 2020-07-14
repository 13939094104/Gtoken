package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.ad.CBid;
import vdsMain.wallet.Wallet;

import java.util.ArrayList;
import java.util.List;

//bkr
public class BidListMessage extends VMessage {

    /* renamed from: a */
    CBid f11853a = new CBid();

    /* renamed from: b */
    List<CBid> f11854b = new ArrayList();

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) {
    }

    public BidListMessage(@NonNull Wallet izVar) {
        super(izVar, "bidls");
    }

    public void onDecodeSerialData() {
        this.f11853a.decodeSerialStream(getTempInput());
        int readInt32 = readInt32();
        for (int i = 0; i < readInt32; i++) {
            CBid biz = new CBid();
            biz.decodeSerialStream(getTempInput());
            this.f11854b.add(biz);
        }
    }

    /* renamed from: a */
    public CBid mo42555a() {
        return this.f11853a;
    }

    /* renamed from: b */
    public List<CBid> mo42556b() {
        return this.f11854b;
    }
}
