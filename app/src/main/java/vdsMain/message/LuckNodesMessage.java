package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.clue.LuckyNodeItem;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.List;

//blz
public class LuckNodesMessage extends VMessage {

    /* renamed from: a */
    public short f11902a;

    /* renamed from: b */
    public List<LuckyNodeItem> f11903b;

    public LuckNodesMessage(@NonNull Wallet izVar) {
        super(izVar, "luckynodes");
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        writeVariableInt((long) this.f11902a);
        writeObjectList(this.f11903b);
    }

    public void onDecodeSerialData() throws IOException {
        this.f11902a = (short) readVariableInt().getIntValue();
        this.f11903b = readObjectList(this.f11903b, LuckyNodeItem.class);
    }
}
