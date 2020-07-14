package vdsMain.block;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.WalletSeriablData;
import vdsMain.transaction.CMerkleTransaction;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.Vector;

//bjc
public class CMerkleTxBlock extends WalletSeriablData {

    /* renamed from: a */
    public UInt256 f11719a = new UInt256();

    /* renamed from: b */
    public Vector<CMerkleTransaction> f11720b;

    public CMerkleTxBlock(@NonNull Wallet izVar) {
        super(izVar);
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        this.f11719a.serialToStream(streamWriter);
        Vector<CMerkleTransaction> vector = this.f11720b;
        if (vector == null || vector.isEmpty()) {
            writeVariableInt(0);
        } else {
            streamWriter.writeObjectList(this.f11720b);
        }
    }

    public void onDecodeSerialData() {
        this.f11719a.decodeSerialStream((SeriableData) this);
        int b = readVariableInt().getIntValue();
        if (b < 1) {
            this.f11720b = null;
            return;
        }
        this.f11720b = new Vector<>(b);
        for (int i = 0; i < b; i++) {
            CMerkleTransaction bqd = new CMerkleTransaction(this.wallet);
            bqd.decodeSerialStream((SeriableData) this);
            bqd.f12072a.setBlockHash(this.f11719a);
            this.f11720b.add(bqd);
        }
    }
}
