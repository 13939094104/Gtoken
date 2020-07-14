package vdsMain.model;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.exceptions.InvalidateBlockException;
import vcash.model.VTxModel;
import vdsMain.ArrayListMap;
import vdsMain.CTxDestination;
import vdsMain.SaplingPaymentAddress;
import vdsMain.SparseArray;
import vdsMain.block.*;
import vdsMain.db.VTransactionDB;
import vdsMain.table.TransactionTable;
import vdsMain.transaction.SaplingMerkleTree;
import vdsMain.transaction.Transaction;
import vdsMain.transaction.VCWalletTx;
import vdsMain.transaction.VTransaction;
import vdsMain.wallet.CWallet;
import vdsMain.wallet.VCWallet;
import vdsMain.wallet.Wallet;

import java.util.*;

//bph
//910 bpw
public class VBlockChainModel extends BlockChainModel {

    //是否同步匿名事务 3是同步1是不同步，先设成1
    private int f12151j = 1;

    public VBlockChainModel(@NonNull Wallet wallet) {
        super(wallet);
    }


    //915 mo42974f
    //mo42739e
    public void setSyncAnymousFlag(int i) {
        this.f12151j = i;
    }

    //mo42740u
    public boolean syncAymousTransaction() {
        return (this.f12151j & 2) != 0;
    }
    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42678a(BlockInfo blockInfo) throws InvalidateBlockException {
        if (!blockInfo.isFirstBlock()) {
            ((VTransactionDB) this.wallet.getSelfTransactionDB()).getCachedSaplingBlockTable().mo42531b(blockInfo.getBlockHash());
            VTxModel vTxModel = (VTxModel) this.wallet.getSelfTransactionModel();
            vTxModel.mo40446a(blockInfo.getBlockNo());
            List<Transaction> transactionList = blockInfo.getSelfTransactionList();
            if (transactionList == null || transactionList.isEmpty()) {
                transactionList = vTxModel.getConfirmedTransactionListFromAll(blockInfo.getBlockHash());
            }
            if (transactionList != null && !transactionList.isEmpty()) {
                for (Transaction transaction : transactionList) {
                    if (transaction instanceof VCWalletTx) {
                        vTxModel.mo40432a(blockInfo.getBlockNo(), (VCWalletTx) transaction);
                    }
                }
            }
            if (((VBlockInfo) checkAndGetBlockInfoByBlockHash(blockInfo.getPreBlockHash(), new boolean[0])) == null) {
                throw new InvalidateBlockException("Could note found previors block");
            }
        }
    }

    //mo42677a
    //910 mo42735a
    public void findRelatedSaplingTransactionAndUpdateVTxSapn(CachedBlockInfo cachedBlockInfo, BlockInfo blockInfo) {
        VTxModel vTxModel = (VTxModel) this.wallet.getSelfTransactionModel();
        if (!blockInfo.isFirstBlock()) {
            VCachedBlockInfo vCachedBlockInfo = (VCachedBlockInfo)cachedBlockInfo;
            SaplingMerkleTree saplingMerkleTree = null;
            if (vCachedBlockInfo != null) {
                saplingMerkleTree = vCachedBlockInfo.mSaplingMerkleTree;
            }
            HashMap<UInt256, VTransaction> vTransactionHashMap = new HashMap();
            vTxModel.findRelatedSaplingTransactionToMap(vCachedBlockInfo, blockInfo.getBlockNo(), vTransactionHashMap);
            if (saplingMerkleTree != null) {
                vTxModel.mo40437a(vCachedBlockInfo);
            }
            if (!vTransactionHashMap.isEmpty()) {
                TransactionTable transactionTable = this.wallet.getSelfTransactionDB().getSelfTransactionTable();
                boolean inTransaction = transactionTable.isInTransaction();
                if (!inTransaction) {
                    transactionTable.beginTransaction();
                }
                for (Map.Entry entry : vTransactionHashMap.entrySet()) {
                    VTransaction vTransaction = (VTransaction) entry.getValue();
                    if (saplingMerkleTree != null) {
                        vTxModel.updateVTxSapn(vTransaction);
                    }
                }
                if (!inTransaction) {
                    transactionTable.endTransaction(true);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    //mo42676a
    public void checkAndReplacSaplingBlock(CachedBlockInfo cachedBlockInfo) {
        VCachedBlockInfo vCachedBlockInfo = (VCachedBlockInfo) cachedBlockInfo;
        if (vCachedBlockInfo.mSaplingMerkleTree != null && vCachedBlockInfo.txidToTransactionLinkedMap != null && !vCachedBlockInfo.txidToTransactionLinkedMap.isEmpty()) {
            ((VTransactionDB) getWalllet().getSelfTransactionDB()).getCachedSaplingBlockTable().checkAndReplaceCachedSaplingBlock(cachedBlockInfo.blockHeader.getBlockHash(), vCachedBlockInfo.mSaplingMerkleTree, vCachedBlockInfo.txidToTransactionLinkedMap.values());
        }
    }

    //910 mo42737a
    public boolean mo42679a(CachedBlockInfo cachedBlockInfo, BlockInfo addBlockInfo, HashMap<CTxDestination, Address> hashMap, SparseArray<Account> accountSparseArray) throws InvalidateBlockException {
        BlockInfo tempBlockInfo;
        ArrayList<CTxDestination> arrayList;
        ArrayList arrayList2;
        ArrayListMap<UInt256, Transaction> waitConfirmedTransactionMap;
        boolean z2;
        ArrayList<UInt256> addTxidList;
        Iterator addTransactionIterator;
        ArrayList<CTxDestination> arrayList5=new ArrayList();;
        VCWallet vcWallet;
        boolean firstBlock = addBlockInfo.isFirstBlock();
        boolean inTransaction = this.blockTable.isInTransaction();
        if (!inTransaction) {
            this.blockTable.beginTransaction();
        }
        VTxModel vTxModel = (VTxModel) this.wallet.getSelfTransactionModel();
        BlockInfo localAddBlockInfo = checkAndGetBlockInfoByBlockHash(addBlockInfo.getBlockHash(), new boolean[0]);
        if (localAddBlockInfo == null) {
            if (!checkAndAddNewBlock(addBlockInfo.getBlockHeader(), !firstBlock)) {
                if (!inTransaction) {
                    this.blockTable.endTransaction(false);
                }
                return false;
            }
            tempBlockInfo = addBlockInfo;
        } else {
            tempBlockInfo = localAddBlockInfo;
        }
        if (!tempBlockInfo.isEqual(addBlockInfo)) {
            if (!inTransaction) {
                this.blockTable.endTransaction(true);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Invalidate block ");
            sb.append(addBlockInfo.getBlockHash());
            sb.append(" , ignore this block data");
            throw new InvalidateBlockException(sb.toString());
        }
        addBlockInfo.setBlockNoAndSetTransactionsHash(tempBlockInfo.getBlockNo());
        if (this.mCurBlockHeader == null || this.mCurBlockHeader.getBlockHash().equals(addBlockInfo.getBlockHash()) || addBlockInfo.getPreBlockHash().equals(this.mCurBlockHeader.getBlockHash())) {
            this.mCurBlockHeader = tempBlockInfo.getBlockHeader();
        }
        List addTransactionList = addBlockInfo.getSelfTransactionList();
        ArrayList<Transaction> arrayList7 = new ArrayList<>();
        arrayList2 = new ArrayList();
        waitConfirmedTransactionMap = new ArrayListMap<UInt256, Transaction>();
        vcWallet = (VCWallet) this.wallet.getSelfCWallet();
        arrayList = new ArrayList();
        if (addTransactionList != null && !addTransactionList.isEmpty())
        {
            addTxidList = new ArrayList<UInt256>();
            this.wallet.getSelfTransactionDB().beginTransaction();
            addTransactionIterator = addTransactionList.iterator();
            while (addTransactionIterator.hasNext()) {
                Transaction addTransaction = (Transaction) addTransactionIterator.next();
                addTxidList.add(addTransaction.getTxid());
                addTransaction.setMBlockNo(addBlockInfo.getBlockNo());
                VTransaction localVTransaction = (VTransaction) vTxModel.getTransactionFromAllTransactionMap(addTransaction.getTxid());
                if (localVTransaction != null && ((VTransaction) addTransaction).isSpendDesciptionListAndOuputDescriptionListNotEmpty()) {
                    vTxModel.mo44507b((Transaction) localVTransaction, (List<Transaction>) new ArrayList<Transaction>());
                    localVTransaction = null;
                }
                if (localVTransaction == null) {
                    addTransaction.setSelfCWallet((CWallet) vcWallet);
                    addTransaction.updateTimeStamp(addBlockInfo.getBlockTime());
                    mo44427a(addTransaction, (List<CTxDestination>) arrayList5, hashMap, accountSparseArray, true);
                    if (vTxModel.processNewTransaction(addTransaction)) {
                        Transaction transaction = vTxModel.getTransactionFromAllTransactionMap(addTransaction.getTxid());
                        transaction.setBlockHash(addBlockInfo.getBlockHash());
                        arrayList7.add(transaction);
                        vTxModel.replaceData(transaction);
                    }
                } else if (!firstBlock) {
                    boolean blockHashNotNull = localVTransaction.isConfirmed();
                    localVTransaction.checkAndAddSaplingUtxoValue();
                    if (!blockHashNotNull) {
                        localVTransaction.setBlockHash(addBlockInfo.getBlockHash());
                        localVTransaction.setMBlockNo(addBlockInfo.getBlockNo());
                        localVTransaction.updateTimeStamp(addBlockInfo.getBlockTime());
                        mo44427a((Transaction) localVTransaction, (List<CTxDestination>) arrayList5, hashMap, accountSparseArray, false);
                        localVTransaction.updateTimeStamp(addBlockInfo.getBlockTime());
                        vTxModel.mo40452b((Transaction) localVTransaction, true);
                        if (!waitConfirmedTransactionMap.hasKey(localVTransaction.getTxid())) {
                            waitConfirmedTransactionMap.addKeyValueSynchronized(localVTransaction.getTxid(), localVTransaction);
                        }
                    } else {
                        localVTransaction.updateTimeStamp(addBlockInfo.getBlockTime());
                        vTxModel.mo40454d(localVTransaction.getTxid());
                    }
                    vTxModel.calAddressWeightAndPutMap(localVTransaction);
                    vTxModel.mo40454d(localVTransaction.getTxid());
                    arrayList7.add(localVTransaction);
                    vTxModel.replaceData(localVTransaction);
                }
            }
            this.confirmedBlockHashToTxidsMap.put(addBlockInfo.getBlockHash(), addTxidList);
        }
        mo44428a(waitConfirmedTransactionMap, (List<Transaction>) arrayList2, accountSparseArray, hashMap);
        tempBlockInfo.setTransactionList(arrayList7);
        tempBlockInfo.setBlockHeaderSynchronized(BlockSyncStatus.SYNCHED);
        this.blockTable.updateBlockInfoStatus(tempBlockInfo);
        addToHashBlockHeaderCacheMap(tempBlockInfo.getBlockHeader());
        findRelatedSaplingTransactionAndUpdateVTxSapn(cachedBlockInfo, tempBlockInfo);
        for (Transaction transaction : arrayList7) {
            arrayList.clear();
            transaction.fillDesListWithRelatedAddress((List<CTxDestination>) arrayList);
            if (!arrayList.isEmpty()) {
                for (CTxDestination des : arrayList) {
                    if (des instanceof SaplingPaymentAddress) {
                        findRelatedAddressAndAccountToMapByDes(des, hashMap, accountSparseArray);
                    }
                }
            }
        }
        if (((long) tempBlockInfo.getBlockNo()) != getMaxBlockNo()) {
            changeChainSyncStatus(ChainSyncStatus.SYNCHING);
        }
        if (!inTransaction) {
            this.blockTable.endTransaction(true);
        }
        if (tempBlockInfo.getBlockSyncStatus() == BlockSyncStatus.SYNCHED) {
            notifyUpdateBlock(tempBlockInfo, (List<Transaction>) arrayList7, hashMap);
        }
        if (!waitConfirmedTransactionMap.isValueListEmpty()) {
            notifyOnTransactionsConfirmed(waitConfirmedTransactionMap.getValueList());
        }
        if (isNewestBlock()) {
            changeChainSyncStatus(ChainSyncStatus.SYNCHED);
            z2 = true;
        } else {
            z2 = true;
        }
        return z2;
    }

    /* renamed from: c */
    public void mo42680c(UInt256 blockHash) throws InvalidateBlockException {
        super.mo42680c(blockHash);
        int n = ((VTxModel) this.wallet.getSelfTransactionModel()).mo40467n();
        if (n > -1) {
            mo44422a(n);
        }
    }
}
