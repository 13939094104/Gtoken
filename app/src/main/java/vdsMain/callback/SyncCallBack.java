package vdsMain.callback;

import vdsMain.BLOCK_CHAIN_TYPE;
import vdsMain.CTxDestination;
import vdsMain.block.BlockHeader;
import vdsMain.block.BlockInfo;
import vdsMain.block.ChainSyncStatus;
import vdsMain.transaction.Transaction;
import vdsMain.model.Address;
import java.util.HashMap;
import java.util.List;

//bar
public interface SyncCallBack {
    //mo39678a
    void OnAdUpdate();

    //mo39679a
    void onTransactionSent(BLOCK_CHAIN_TYPE blockChainType, Transaction transaction);

    //mo39680a
    void onBlockNoUpdate(BLOCK_CHAIN_TYPE blockChainType, ChainSyncStatus chainSyncStatus, BlockHeader blockHeader, int i);

    //mo39681a
    void onTransactionsConfirmed(BLOCK_CHAIN_TYPE blockChainType, List<Transaction> list);

    //mo39682a
    void onTransactionsInvalid(BLOCK_CHAIN_TYPE blockChainType, List<BlockHeader> list, List<Transaction> list2, HashMap<CTxDestination, Address> hashMap);

    //mo39683a
    void onUpdateBlock(BLOCK_CHAIN_TYPE blockChainType, BlockInfo blockInfo, List<Transaction> list, HashMap<CTxDestination, Address> hashMap);

    //mo39684a
    void onResynched(boolean z);

    //mo39685b
    void onTransactionAbanded(BLOCK_CHAIN_TYPE blockChainType, Transaction transaction);

    //mo39686b
    void onTransactionsReceived(BLOCK_CHAIN_TYPE blockChainType, List<Transaction> list);

    /* renamed from: c */
    void mo39687c(BLOCK_CHAIN_TYPE blockChainType, List<Transaction> list);
}
