package vdsMain.transaction;

import bitcoin.CKeyStore;

//bpq
public class VMutableTransactionSignatureCreator extends VTransactionSignatureCreator {

    /* renamed from: e */
    private VTransaction f12037e;

    public VMutableTransactionSignatureCreator(CKeyStore cKeyStore, VTransactionInterface transactionInterface, int i, int... nHashType) {
        super(cKeyStore, transactionInterface, i, nHashType);
        this.f12037e = (VTransaction) transactionInterface.getWallet().getSelfWalletHelper().mo44131a((TransactionInteface) transactionInterface);
    }
}

