package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.block.CMerkleTxBlockSample;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

//bmh
public class STX2Message extends VMessage {

    //f11915a
    public int startHeight;

    //f11916b
    public int endHeight;

    //f11917h
    public List<CMerkleTxBlockSample> cMerkleTxBlockList;

    public STX2Message(@NonNull Wallet izVar) {
        super(izVar, "stx2");
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeUInt32T((long) this.startHeight);
        streamWriter.writeUInt32T((long) this.endHeight);
        writeObjectList(this.cMerkleTxBlockList);
    }

    public void onDecodeSerialData() throws IOException {
        super.onDecodeSerialData();
        this.startHeight = (int) readUInt32();
        this.endHeight = (int) readUInt32();
        int b = readVariableInt().getIntValue();
        if (b < 1) {
            this.cMerkleTxBlockList = null;
            return;
        }
        this.cMerkleTxBlockList = new Vector(b);
        for (int i = 0; i < b; i++) {
            CMerkleTxBlockSample bjd = new CMerkleTxBlockSample(this.wallet);
            bjd.decodeSerialStream((SeriableData) this);
            this.cMerkleTxBlockList.add(bjd);
        }
    }
}
