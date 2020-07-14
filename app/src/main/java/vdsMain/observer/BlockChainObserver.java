package vdsMain.observer;

import vdsMain.BLOCK_CHAIN_TYPE;
import vdsMain.CTxDestination;
import vdsMain.block.BlockHeader;
import vdsMain.block.BlockInfo;
import vdsMain.block.ChainSyncStatus;
import vdsMain.transaction.Transaction;
import vdsMain.model.Address;
import java.util.HashMap;
import java.util.List;

public interface BlockChainObserver {
    //mo39592a
    void onCurBlockNoChange(BLOCK_CHAIN_TYPE blockChainType, int i, int cur, int max);

    //mo39593a
    void onBlockNoUpdate(BLOCK_CHAIN_TYPE block_chain_type, ChainSyncStatus chainSyncStatus, BlockHeader blockHeader, int maxBlockNo);

    //mo39594a
    void onTransactionsInvalid(BLOCK_CHAIN_TYPE igVar, List<BlockHeader> list, List<Transaction> list2, HashMap<CTxDestination, Address> hashMap);

    //mo39595a
    void onUpdateBlock(BLOCK_CHAIN_TYPE blockChainType, BlockInfo blockInfo, List<Transaction> list, HashMap<CTxDestination, Address> hashMap);
}
