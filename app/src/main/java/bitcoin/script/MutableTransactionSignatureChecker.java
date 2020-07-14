package bitcoin.script;

import vdsMain.transaction.CMutableTransaction;
import vdsMain.transaction.PrecomputedTransactionData;

public class MutableTransactionSignatureChecker extends GenericTransactionSignatureChecker<CMutableTransaction> {
    public MutableTransactionSignatureChecker(CMutableTransaction dfVar, int i, long j) {
        super(dfVar, i, j);
    }

    public MutableTransactionSignatureChecker(CMutableTransaction dfVar, int i, long j, PrecomputedTransactionData cqVar) {
        super(dfVar, i, j, cqVar);
    }
}
