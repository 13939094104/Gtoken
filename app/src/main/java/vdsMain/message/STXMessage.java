package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.block.CMerkleTxBlock;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

//bmi
public class STXMessage extends VMessage {

    /* renamed from: a */
    public int f11918a;

    /* renamed from: b */
    public int f11919b;

    /* renamed from: h */
    public List<CMerkleTxBlock> f11920h;

    public STXMessage(@NonNull Wallet izVar) {
        super(izVar, "stx");
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeUInt32T((long) this.f11918a);
        streamWriter.writeUInt32T((long) this.f11919b);
        writeObjectList(this.f11920h);
    }

    public void onDecodeSerialData() throws IOException {
        super.onDecodeSerialData();
        this.f11918a = (int) readUInt32();
        this.f11919b = (int) readUInt32();
        int b = readVariableInt().getIntValue();
        if (b < 1) {
            this.f11920h = null;
            return;
        }
        this.f11920h = new Vector(b);
        for (int i = 0; i < b; i++) {
            CMerkleTxBlock bjc = new CMerkleTxBlock(this.wallet);
            bjc.decodeSerialStream((SeriableData) this);
            this.f11920h.add(bjc);
        }
    }
}
