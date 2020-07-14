package vdsMain.transaction;

import bitcoin.BaseBlob;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import net.bither.bitherj.utils.Utils;

import java.io.IOException;
import java.util.Arrays;

public class COutPoint extends SeriableData {

    //f12272a
    public UInt256 txid = new UInt256();

    //f12273b
    public int index = -1;

    //f12274c
    protected int hashcode = 0;

    //mo43132b
    public int getLength() {
        return 36;
    }

    public COutPoint() {
        clear();
    }

    public COutPoint(UInt256 uInt256, int i) {
        this.txid.set((BaseBlob) uInt256);
        this.index = i;
        copyTxidAndIndex();
    }

    //mo43129a
    public void initTxidAndIndex(UInt256 txid, int i) {
        this.txid = txid;
        this.index = i;
        copyTxidAndIndex();
    }

    /* renamed from: a */
    public void mo43128a(int i) {
        this.index = i;
        copyTxidAndIndex();
    }

    //mo43127a
    public void clear() {
        this.txid.setNull();
        this.index = -1;
        this.hashcode = 0;
    }

    /* renamed from: a */
    public void mo43130a(COutPoint ciVar) {
        this.txid.set((BaseBlob) ciVar.txid);
        this.index = ciVar.index;
        this.hashcode = ciVar.hashcode;
    }

    //mo43131a
    public void setTxidAndInexFromOther(TxOut txOut) {
        this.txid.set((BaseBlob) txOut.txid);
        this.index = txOut.getIndex();
        copyTxidAndIndex();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof COutPoint)) {
            return false;
        }
        COutPoint ciVar = (COutPoint) obj;
        if (!ciVar.txid.equals(this.txid) || ciVar.index != this.index) {
            z = false;
        }
        return z;
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeUInt256(this.txid);
        streamWriter.writeUInt32T((long) this.index);
    }

    public void onDecodeSerialData() {
        this.txid.decodeSerialStream(getTempInput());
        String txidStr=this.txid.hashString();
        this.index = (int) readUInt32();
        copyTxidAndIndex();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("COutPoint(");
        sb.append(this.txid.hashString());
        sb.append(", ");
        sb.append(this.index);
        sb.append(")");
        return sb.toString();
    }

    public int hashCode() {
        return this.hashcode;
    }

    /* access modifiers changed from: protected */
    //mo43133c
    public void copyTxidAndIndex() {
        byte[] bArr = new byte[36];
        System.arraycopy(this.txid.data(), 0, bArr, 0, 32);
        Utils.uint32ToByteArrayLE((long) this.index, bArr, 32);
        this.hashcode = Arrays.hashCode(bArr);
    }

    /* renamed from: d */
    public COutPoint clone() {
        COutPoint ciVar = new COutPoint();
        ciVar.txid.set((BaseBlob) this.txid);
        ciVar.index = this.index;
        ciVar.hashcode = this.hashcode;
        return ciVar;
    }
}