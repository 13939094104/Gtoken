package vdsMain.transaction;

import bitcoin.BaseBlob;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import org.jetbrains.annotations.NotNull;
import vdsMain.WalletSeriablData;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class CMutableTransaction extends WalletSeriablData implements TransactionInteface {

    //f12395a
    public List<TxIn> txInList = new Vector();

    //f12396b
    public List<TxOut> txOutList = new Vector();

    //f12397c
    public long version;

    //f12398d
    public long lockTime;

    //f12399e
    public short flag;

    //f12400f
    public UInt256 txId;

    //f12401g
    public int blockNo = -1;

    public CMutableTransaction(Transaction transaction) {
        super(transaction.mo44660R());
        this.txId = new UInt256((BaseBlob) transaction.getTxId());
        cloneTxInList(transaction.getSelfTxInList());
        cloneTxOutList(transaction.getSelfTxOutList());
        this.version = transaction.getVersion();
        this.lockTime = transaction.getLockTime();
        this.flag = (short) transaction.getFlag();
        this.blockNo = transaction.getBlockNo();
    }

    public CMutableTransaction(CMutableTransaction cMutableTransaction) {
        super((WalletSeriablData) cMutableTransaction);
        if (cMutableTransaction != this) {
            this.txId = new UInt256((BaseBlob) cMutableTransaction.getTxId());
            cloneTxInList(cMutableTransaction.getSelfTxInList());
            cloneTxOutList(cMutableTransaction.getSelfTxOutList());
            this.version = cMutableTransaction.version;
            this.lockTime = cMutableTransaction.lockTime;
            this.flag = cMutableTransaction.flag;
            this.blockNo = cMutableTransaction.blockNo;
        }
    }

    //mo43215a
    public void cloneTxInList(List<TxIn> list) {
        if (list != null && !list.isEmpty()) {
            for (TxIn txIn : list) {
                this.txInList.add(txIn.clone());
            }
        }
    }

    //mo43217b
    public void cloneTxOutList(List<TxOut> list) {
        if (list != null && !list.isEmpty()) {
            for (TxOut txOut : list) {
                this.txOutList.add(txOut.clone());
            }
        }
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        TransactionSerializer.m10970a((TransactionInteface) this, streamWriter, new boolean[0]);
    }

    public void onDecodeSerialData() throws IOException {
        TransactionSerializer.m10985e(this, this);
    }

    /* renamed from: i_ */
    public UInt256 updateTxidByContent() {
        this.txId.clear();
        try {
            CHashWriter kVar = new CHashWriter();
            TransactionSerializer.m10970a((TransactionInteface) this, (StreamWriter) kVar, false);
            mo43214a(kVar.GetHash());
        } catch (IOException e) {
            e.printStackTrace();
            mo43214a(UInt256.empty());
        }
        return this.txId;
    }

    /* renamed from: b */
    public CMutableTransaction clone() {
        return new CMutableTransaction(this);
    }

    /* renamed from: h_ */
    public UInt256 getTxId() {
        return this.txId;
    }

    /* renamed from: a */
    public void mo43214a(UInt256 uInt256) {
        this.txId.set((BaseBlob) uInt256);
        for (TxIn a : this.txInList) {
            a.setMTxid(uInt256);
        }
        for (TxOut a2 : this.txOutList) {
            a2.setMTxid(uInt256);
        }
    }

    /* renamed from: d */
    public List<TxOut> getSelfTxOutList() {
        return this.txOutList;
    }

    /* renamed from: e */
    public List<TxIn> getSelfTxInList() {
        return this.txInList;
    }

    /* renamed from: f */
    public long getVersion() {
        return this.version;
    }

    /* renamed from: g */
    public long getLockTime() {
        return this.lockTime;
    }

    /* renamed from: a */
    public void setVersion(long j) {
        this.version = j;
    }

    /* renamed from: b */
    public void checkAndSetLockTime(long j) {
        this.lockTime = j;
    }

    /* renamed from: h */
    public int getFlag() {
        return this.flag;
    }

    /* renamed from: a */
    public void setFlag(int i) {
        this.flag = (short) i;
        for (TxIn b : this.txInList) {
            b.setFlag(i);
        }
        for (TxOut b2 : this.txOutList) {
            b2.setFlag(i);
        }
    }

    @NotNull
    @Override
    public Wallet getWallet() {
        return wallet;
    }
}
