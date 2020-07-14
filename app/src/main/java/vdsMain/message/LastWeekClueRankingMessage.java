package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.clue.LastWeekClueItem;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

//bly
public class LastWeekClueRankingMessage extends VMessage {

    /* renamed from: a */
    public long f11899a;

    /* renamed from: b */
    public BigInteger f11900b;

    /* renamed from: h */
    public List<LastWeekClueItem> f11901h;

    public LastWeekClueRankingMessage(@NonNull Wallet izVar) {
        super(izVar, "clrk");
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        writeVariableInt(this.f11899a);
        writeUInt64(this.f11900b);
        writeObjectList(this.f11901h);
    }

    public void onDecodeSerialData() throws IOException {
        this.f11899a = readVariableInt().getValue();
        this.f11900b = readUInt64();
        this.f11901h = readObjectList(this.f11901h, LastWeekClueItem.class);
    }
}
