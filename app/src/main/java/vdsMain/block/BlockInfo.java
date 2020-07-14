package vdsMain.block;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.Collection;
import vdsMain.WalletSerializedAbstractTableItem;
import vdsMain.transaction.Transaction;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

public abstract class BlockInfo extends WalletSerializedAbstractTableItem {

    //f13142a
    protected BlockHeader mBlockHeader = null;

    //f13143b
    protected List<Transaction> transactionList = null;

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public abstract void mo41009a(BlockHeader jtVar);

    public BlockInfo(@NonNull BlockHeader blockHeader) {
        super((WalletSerializedAbstractTableItem) blockHeader);
        mo41009a(blockHeader);
        this.mBlockHeader = blockHeader;
    }

    //mo44311d
    //910 mo44386d
    public int getBlockNo() {
        return this.mBlockHeader.blockNo;
    }

    //mo44312e
    //910 mo44387e
    public UInt256 getBlockHash() {
        return this.mBlockHeader.blockHash;
    }

    //mo44314f
    //910 mo44389f
    public UInt256 getPreBlockHash() {
        return this.mBlockHeader.preBlockHash;
    }

    //mo44315g
    //910 mo44390g
    public long getBlockTime() {
        return this.mBlockHeader.blockTime;
    }

    //mo44306a
    //910 mo44381a
    public synchronized void setBlockNoAndSetTransactionsHash(int i) {
        this.mBlockHeader.setBlockNo(i);
        if (this.transactionList != null) {
            for (Transaction transaction : this.transactionList) {
                transaction.setBlockHash(this.mBlockHeader.blockHash);
            }
        }
    }

    //mo44316h
    //910 mo44391h
    public List<Transaction> getSelfTransactionList() {
        return this.transactionList;
    }

    //mo44309a
    //910 mo44384a
    public synchronized void setTransactionList(List<Transaction> list, boolean... zArr) {
        this.transactionList = list;
        if ((zArr.length == 0 || zArr[0]) && list != null) {
            for (Transaction transaction : list) {
                setTransactionBlockHash(transaction);
            }
        }
    }

    /* renamed from: a */
    public synchronized void mo44307a(Transaction dhVar) {
        if (this.transactionList == null) {
            this.transactionList = new Vector();
        }
        this.transactionList.add(dhVar);
        setTransactionBlockHash(dhVar);
    }

    //910 mo44392i
    //mo44317i
    public BlockSyncStatus getBlockSyncStatus() {
        return this.mBlockHeader.blockSyncStatus;
    }

    //mo44308a
    //910 mo44383a
    public synchronized void setBlockHeaderSynchronized(BlockSyncStatus ikVar) {
        this.mBlockHeader.setBlockSyncStatus(ikVar);
    }

    //m12621b
    private void setTransactionBlockHash(Transaction transaction) {
        transaction.setBlockHash(this.mBlockHeader.blockHash);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(this.mBlockHeader.toString());
        stringBuffer.append("\ntransactions: ");
        if (this.transactionList == null) {
            stringBuffer.append("0");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(this.transactionList.size());
            sb.append("\n[\n");
            stringBuffer.append(sb.toString());
            for (Transaction dhVar : this.transactionList) {
                stringBuffer.append("\n\t\t");
                stringBuffer.append(dhVar.getTxidHashString());
            }
            stringBuffer.append("\n]");
        }
        return stringBuffer.toString();
    }

    //mo44310a
    //910 mo44385a
    public boolean isEqual(BlockInfo blockInfo) {
        return blockInfo.mBlockHeader.equals(this.mBlockHeader);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        this.mBlockHeader.writeSerialData(streamWriter);
        List<Transaction> list = this.transactionList;
        if (list == null || list.isEmpty()) {
            streamWriter.writeVariableInt(0);
            return;
        }
        streamWriter.writeVariableInt((long) this.transactionList.size());
        for (Transaction c : this.transactionList) {
            c.mo44659c(streamWriter);
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void decodeSerialData(SeriableData seriableData) {
        List<Transaction> list = this.transactionList;
        if (list != null) {
            list.clear();
        }
        this.mBlockHeader.decodeSerialData(seriableData);
        long txCount = seriableData.readVariableInt().getValue();
        if (txCount > 0 && this.transactionList == null) {
            this.transactionList = new Vector();
        }
        for (int i = 0; ((long) i) < txCount; i++) {
            Transaction transaction = this.wallet.getSelfWalletHelper().getNewTransaction();
            transaction.decodeSerialItem(seriableData.getTempInput());
            transaction.setTimeStamp(this.mBlockHeader.blockTime);
            transaction.updateTxidByContent();
            this.transactionList.add(transaction);
        }
    }

    //mo44318j
    //910 mo44393j
    public final BlockHeader getBlockHeader() {
        return this.mBlockHeader;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof BlockInfo) {
            BlockInfo juVar = (BlockInfo) obj;
            if (juVar.getBlockHeader().equals(this.mBlockHeader) && !Collection.m11556b(this.transactionList, juVar.transactionList)) {
                return false;
            }
        }
        return false;
    }

    /* renamed from: a */
    public void initTableItemVariable(Cursor cursor, int i, int i2, int i3) {
        this.mBlockHeader.initTableItemVariable(cursor, i, i2, i3);
    }

    /* renamed from: c */
    public ContentValues getContentValues() {
        return this.mBlockHeader.getContentValues();
    }

    /* renamed from: a */
    public String getKey() {
        return this.mBlockHeader.getKey();
    }

    /* renamed from: e_ */
    public String getValue() {
        return this.mBlockHeader.getValue();
    }

    //mo44319k
    //910 mo44394k
    public boolean isFirstBlock() {
        return this.mBlockHeader.isFirstBlock();
    }
}
