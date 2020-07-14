package vdsMain.message;


import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.clue.TopClueItem;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

//blx
public class LastSeasonTopClueListMessage extends VMessage {

    /* renamed from: a */
    public BigInteger f11895a;

    /* renamed from: b */
    public BigInteger f11896b;

    /* renamed from: h */
    public long f11897h;

    //f11898i
    public List<TopClueItem> topClueItemList;

    public LastSeasonTopClueListMessage(@NonNull Wallet izVar) {
        super(izVar, "topclist");
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        writeUInt64(this.f11895a);
        writeUInt64(this.f11896b);
        writeUInt32T(this.f11897h);
        writeObjectList(this.topClueItemList);
    }

    public void onDecodeSerialData() {
        this.f11895a = readUInt64();
        this.f11896b = readUInt64();
        this.f11897h = (long) readInt32();
        int readInt32 = readInt32();
        this.topClueItemList = new ArrayList();
        for (int i = 0; i < readInt32; i++) {
            TopClueItem bjy = new TopClueItem();
            bjy.decodeSerialStream(getTempInput());
            this.topClueItemList.add(bjy);
        }
    }
}
