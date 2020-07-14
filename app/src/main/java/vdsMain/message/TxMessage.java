package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.transaction.Transaction;
import vdsMain.wallet.Wallet;

//bmk
public class TxMessage extends Message implements TxMessageInterface {

    //f12251a
    private Transaction transaction = null;

    public TxMessage(@NonNull Wallet wallet) {
        super(wallet, "tx");
    }

    public TxMessage(Transaction transaction) {
        super(transaction.mo44660R(), "tx");
        this.transaction = transaction;
    }

    public void onDecodeSerialData() {
        this.transaction = this.wallet.getSelfWalletHelper().getNewTransaction();
        this.transaction.decodeSerialItem(getTempInput());
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) {
        this.transaction.mo44659c(streamWriter);
    }

    /* renamed from: a */
    public Transaction getTransaction() {
        return this.transaction;
    }
}