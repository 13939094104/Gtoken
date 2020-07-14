package vdsMain.transaction;

public class TxSendResult {

    //f13358a
    public Transaction transaction;

    //f13359b
    public boolean isSuccess;

    TxSendResult() {
    }

    public TxSendResult(Transaction transaction, boolean z) {
        this.transaction = transaction;
        this.isSuccess = z;
    }
}