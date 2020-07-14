package vdsMain.transaction;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

//bqd
public class CMerkleTransaction extends SeriableData {

    /* renamed from: a */
    public Transaction f12072a;

    /* renamed from: b */
    public List<UInt256> f12073b = new Vector();

    /* renamed from: c */
    public int f12074c;

    /* renamed from: d */
    public SaplingMerkleTree f12075d;

    /* renamed from: e */
    public Wallet f12076e;

    public CMerkleTransaction(@NonNull Wallet izVar) {
        this.f12076e = izVar;
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeUInt32T((long) this.f12074c);
        Transaction dhVar = this.f12072a;
        if (dhVar == null) {
            this.f12076e.getSelfWalletHelper().getNewTransaction().mo44659c(streamWriter);
        } else {
            dhVar.mo44659c(streamWriter);
        }
        List<UInt256> list = this.f12073b;
        if (list == null || list.isEmpty()) {
            streamWriter.writeVariableInt(0);
        } else {
            streamWriter.writeVariableInt((long) this.f12073b.size());
            for (UInt256 uInt256 : this.f12073b) {
                uInt256.serialToStream((StreamWriter) uInt256);
            }
        }
        streamWriter.writeOptionalObject(this.f12075d);
    }

    public void onDecodeSerialData() {
        this.f12074c = (int) readUInt32();
        this.f12072a = this.f12076e.getSelfWalletHelper().getNewTransaction();
        this.f12072a.decodeSerialItem(this.mTempBuffer);
        this.f12073b.clear();
        for (long b = (long) readVariableInt().getIntValue(); b > 0; b--) {
            UInt256 uInt256 = new UInt256();
            uInt256.decodeSerialStream(this.mTempBuffer);
            this.f12073b.add(uInt256);
        }
        this.f12075d = (SaplingMerkleTree) readOptionalObject(SaplingMerkleTree.class);
    }
}
