package vdsMain.transaction;

public class TxSignatureResult {

    //f13360a
    public Transaction transaction;

    //f13361b
    public OfflineTransaction offlineTransaction;

    //f13362c
    public boolean isSuccess;

    //f13363d
    public TxInfo txInfo;

    public TxSignatureResult() {
    }

    public TxSignatureResult(Transaction transaction, boolean z) {
        this.transaction = transaction;
        this.isSuccess = z;
    }

    public TxSignatureResult(OfflineTransaction offlineTransaction, boolean z) {
        this.offlineTransaction = offlineTransaction;
        if (offlineTransaction != null) {
            this.transaction = offlineTransaction.f13338a;
        }
        this.isSuccess = z;
    }

    //mo44690a
    public void setTxInfo(TxInfo txInfo) {
        this.txInfo = txInfo;
    }
}
