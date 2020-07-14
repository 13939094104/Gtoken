package vdsMain.transaction;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class VOutList extends SeriableData {

    //f13371a
    public List<TxOut> txOutList;

    /* renamed from: b */
    public Wallet f13372b;

    public VOutList() {
        this.txOutList = null;
    }

    public VOutList(@NonNull Wallet izVar) {
        this.txOutList = new ArrayList();
        this.f13372b = izVar;
    }

    public VOutList(@NonNull Wallet izVar, int i) {
        this.txOutList = new ArrayList(i);
        this.f13372b = izVar;
    }

    /* renamed from: a */
    public int mo44704a() {
        List<TxOut> list = this.txOutList;
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return this.txOutList.size();
    }

    //mo44707b
    public boolean isVOutListEmpty() {
        List<TxOut> list = this.txOutList;
        return list == null || list.isEmpty();
    }

    //mo44708c
    public Iterator<TxOut> getTxOutIterator() {
        return this.txOutList.iterator();
    }

    //mo44706a
    public void addTxOutToList(TxOut txOut) {
        if (this.txOutList == null) {
            this.txOutList = new Vector();
        }
        this.txOutList.add(txOut);
    }

    //mo44705a
    public TxOut getTxOutByIndex(int i) {
        return (TxOut) this.txOutList.get(i);
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        List<TxOut> list = this.txOutList;
        if (list == null || list.isEmpty()) {
            streamWriter.writeVariableInt(0);
            return;
        }
        Vector vector = new Vector(this.txOutList.size());
        streamWriter.writeVariableInt((long) this.txOutList.size());
        for (TxOut dnVar : this.txOutList) {
            if (dnVar == null) {
                vector.add(null);
            } else {
                vector.add(dnVar.txid);
            }
            streamWriter.writeOptionalObject(dnVar);
        }
        streamWriter.writeOptionObjectList(vector);
    }

    public void onDecodeSerialData() throws IOException {
        this.txOutList = new Vector();
        readOptionalObjectList(this.txOutList, this.f13372b.getSelfWalletHelper().mo42420f(), Wallet.class, this.f13372b);
        if (this.txOutList.isEmpty()) {
            this.txOutList = null;
            return;
        }
        Vector vector = new Vector(this.txOutList.size());
        readOptionalObjectList(vector, UInt256.class);
        Iterator it = vector.iterator();
        for (TxOut dnVar : this.txOutList) {
            UInt256 uInt256 = (UInt256) it.next();
            if (!(dnVar == null || uInt256 == null)) {
                dnVar.setMTxid(uInt256);
            }
        }
    }
}
