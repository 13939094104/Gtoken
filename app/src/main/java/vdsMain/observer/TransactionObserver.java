package vdsMain.observer;

import vdsMain.BLOCK_CHAIN_TYPE;
import vdsMain.transaction.Transaction;

import java.util.List;

public interface TransactionObserver {
    /* renamed from: a */
    void mo39601a();

    //mo39602a
    void onTransactionSent(BLOCK_CHAIN_TYPE blockChainType, Transaction transaction);

    //mo39603a
    void onTransactionsConfirmed(BLOCK_CHAIN_TYPE blockChainType, List<Transaction> list);

    //mo39604b
    void onTransactionAbanded(BLOCK_CHAIN_TYPE blockChainType, Transaction transaction);

    /* renamed from: b */
    void mo39605b(BLOCK_CHAIN_TYPE blockChainType, List<Transaction> list);

    //mo39606c
    void onTransactionsReceived(BLOCK_CHAIN_TYPE blockChainType, List<Transaction> list);
}