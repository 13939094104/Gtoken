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

//baq
public abstract class SimpleSyncCallBack implements SyncCallBack {
    /* renamed from: a */
    public void OnAdUpdate() {
    }

    /* renamed from: a */
    public void onTransactionSent(BLOCK_CHAIN_TYPE blockChainType, Transaction transaction) {
    }

    /* renamed from: a */
    public void onBlockNoUpdate(BLOCK_CHAIN_TYPE igVar, ChainSyncStatus chainSyncStatus, BlockHeader jtVar, int i) {
    }

    //mo39681a
    public void onTransactionsConfirmed(BLOCK_CHAIN_TYPE igVar, List<Transaction> list) {
    }

    /* renamed from: a */
    public void onTransactionsInvalid(BLOCK_CHAIN_TYPE igVar, List<BlockHeader> list, List<Transaction> list2, HashMap<CTxDestination, Address> hashMap) {
    }

    /* renamed from: a */
    public void onUpdateBlock(BLOCK_CHAIN_TYPE igVar, BlockInfo blockInfo, List<Transaction> list, HashMap<CTxDestination, Address> hashMap) {
    }

    /* renamed from: a */
    public void onResynched(boolean z) {
    }

    /* renamed from: b */
    public void onTransactionAbanded(BLOCK_CHAIN_TYPE igVar, Transaction dhVar) {
    }

    /* renamed from: b */
    public void onTransactionsReceived(BLOCK_CHAIN_TYPE blockChainType, List<Transaction> list) {
    }

    /* renamed from: c */
    public void mo39687c(BLOCK_CHAIN_TYPE igVar, List<Transaction> list) {
    }
}