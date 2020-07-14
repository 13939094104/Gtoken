package bitcoin.script;


import vdsMain.transaction.PrecomputedTransactionData;
import vdsMain.transaction.Transaction;

public class TransactionSignatureChecker extends GenericTransactionSignatureChecker<Transaction> {
    public TransactionSignatureChecker(Transaction dhVar, int i, long j) {
        super(dhVar, i, j);
    }

    public TransactionSignatureChecker(Transaction dhVar, int i, long j, PrecomputedTransactionData cqVar) {
        super(dhVar, i, j, cqVar);
    }
}