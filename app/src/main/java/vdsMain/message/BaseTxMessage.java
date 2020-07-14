package vdsMain.message;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.transaction.Transaction;
import vdsMain.transaction.VTransaction;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//910 blc
public class BaseTxMessage extends VMessage {

    //910 f11976a
    private UInt256 blockHash;

    //910 f11977b
    private List<Transaction> transactionList = new ArrayList();

    public BaseTxMessage(@NonNull Wallet izVar) {
        super(izVar, "basetx");
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        this.blockHash.serialToStream(streamWriter);
        writeObjectList(this.transactionList);
    }

    public void onDecodeSerialData() {
        this.blockHash = new UInt256();
        this.blockHash.decodeSerialStream(getTempInput());
        int b = readVariableInt().getIntValue();
        for (int i = 0; i < b; i++) {
            VTransaction vTransaction = new VTransaction(this.wallet);
            vTransaction.mo44658c((SeriableData) this);
            vTransaction.mo43116af();
            this.transactionList.add(vTransaction);
        }
    }

    //910 mo42605a
    public UInt256 getBlockHash() {
        return this.blockHash;
    }

    //910 mo42606b
    public List<Transaction> getTransactionList() {
        return this.transactionList;
    }
}
