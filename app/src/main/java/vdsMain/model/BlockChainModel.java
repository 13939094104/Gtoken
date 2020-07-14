package vdsMain.model;

import androidx.annotation.NonNull;
import bitcoin.BaseBlob;
import bitcoin.UInt256;
import bitcoin.consensus.ArithUint256;
import com.facebook.stetho.server.http.HttpStatus;
import generic.exceptions.InvalidateBlockException;
import vdsMain.*;
import vdsMain.block.*;
import vdsMain.db.TransactionDB;
import vdsMain.table.AbstractTableItem;
import vdsMain.table.BlockTable;
import vdsMain.transaction.Transaction;
import vdsMain.transaction.TxIn;
import vdsMain.wallet.ChainParams;
import vdsMain.wallet.Wallet;

import java.util.*;
import java.util.Collection;

public abstract class BlockChainModel extends Model {

    //f13196a
    //910 f13334a
    protected BlockTable blockTable;

    //f13197b
    protected ChainSyncStatus mChainSyncStatus = ChainSyncStatus.UNSYNC;

    //910 f13336c
    protected BlockHeader mCurBlockHeader;

    //f13199d
    protected BlockIndex blockIndex = new BlockIndex();

    //f13200e
    protected LinkedHashMap<UInt256, BlockHeader> blockHashToBlockHeaderCacheMap = new LinkedHashMap<>();

    //f13201f
    //910 f13340g
    protected LinkedHashMap<UInt256, List<UInt256>> confirmedBlockHashToTxidsMap = new LinkedHashMap<>();

    //f13202g
    protected BlockChainModelEvent mBlockChainModelEvent;

    //f13203i
    //910 f13342j
    private long maxBlockNo = 0;

    /* renamed from: ld$a */
    //C3888a
    public interface BlockChainModelEvent {
        //mo44054a
        void onCurBlockNoChange(int i, int i2, int i3);

        /* renamed from: a */
        void mo44063a(List<BlockHeader> list, List<Transaction> list2, HashMap<CTxDestination, Address> hashMap);

        //mo44066a
        void onBlockNoUpdate(BlockHeader jtVar, int i, ChainSyncStatus ioVar);

        //mo44067a
        void onUpdateBlock(BlockInfo juVar, List<Transaction> list, HashMap<CTxDestination, Address> hashMap);

        //910 mo44159b
        void notifyOnTransactionsConfirmed(List<Transaction> list);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void checkAndReplacSaplingBlock(CachedBlockInfo imVar) {
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public abstract void findRelatedSaplingTransactionAndUpdateVTxSapn(CachedBlockInfo imVar, BlockInfo juVar);

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public abstract void mo42678a(BlockInfo juVar) throws InvalidateBlockException;

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public abstract boolean mo42679a(CachedBlockInfo imVar, BlockInfo juVar, HashMap<CTxDestination, Address> hashMap, SparseArray<Account> gtVar) throws InvalidateBlockException;

    public BlockChainModel(@NonNull Wallet izVar) {
        super(izVar);
    }

    //mo44434a
    public synchronized void setBlockChainModelEvent(BlockChainModelEvent event) {
        this.mBlockChainModelEvent = event;
    }


    //910 mo44529c
    public Pair<UInt256, UInt256> getFirstUnsyncBlockHashToCurBlockHashPair() {
        UInt256 firstUnsyncBlockHash = this.blockTable.getFirstUnsyncBlockHash();
        if (firstUnsyncBlockHash != null) {
            BlockHeader mCurBlockHeader = this.mCurBlockHeader;
            if (mCurBlockHeader != null) {
                return new Pair<>(firstUnsyncBlockHash, mCurBlockHeader.getBlockHash());
            }
        }
        return null;
    }

    //mo44416a
    //910 mo44495a
    public int getCurrentBlockNo() {
        BlockHeader blockHeader = this.mCurBlockHeader;
        if (blockHeader != null) {
            return blockHeader.getBlockNo();
        }
        return 0;
    }

    //mo44420a
    public BlockInfo getBlockInfoFromDb(long no, boolean... zArr) {
        if (no < 0 || no > ((long) getCurrentBlockNo())) {
            return null;
        }
        return getBlockInfoFromBlockHeader(this.blockTable.getBlockHeaderFromDbByNo(no), zArr);
    }

    //mo44419a
    public BlockHeader checkAndGetBlockHeaderFromDb(long no) {
        if (no < 0 || no > ((long) this.blockIndex.blockNo)) {
            return null;
        }
        return this.blockTable.getBlockHeaderFromDbByNo(no);
    }

    //mo44439b
    public int getFirstBlockHeaderAfterTime(long time) {
        BlockHeader blockHeader = this.blockTable.getFirstBlockHeaderAfterTime(time);
        if (blockHeader != null) {
            return blockHeader.getBlockNo();
        }
        return getCurrentBlockNo();
    }

    //mo44436a
    public boolean isBlockSynched(UInt256 uInt256) {
        BlockHeader blockHeader = (BlockHeader) this.blockHashToBlockHeaderCacheMap.get(uInt256);
        if ((blockHeader == null || blockHeader.getBlockSyncStatus() != BlockSyncStatus.SYNCHED) && !this.blockTable.getSynchedBlockByHash(uInt256)) {
            return false;
        }
        return true;
    }

    //mo44421a
    //mo44500a 910
    public BlockInfo checkAndGetBlockInfoByBlockHash(UInt256 uInt256, boolean... zArr) {
        if (uInt256 == null) {
            return null;
        }
        return getBlockInfoFromBlockHeader(getBlockHeaderFromCachedAndDbByHash(uInt256), zArr);
    }

    //mo44440b
    //910 mo44522b
    public BlockHeader getBlockHeaderFromCachedAndDbByHash(UInt256 uInt256) {
        if (uInt256 == null) {
            return null;
        }
        BlockHeader blockHeader = (BlockHeader) this.blockHashToBlockHeaderCacheMap.get(uInt256);
        if (blockHeader == null) {
            blockHeader = this.blockTable.getBlockHeaderFromDbByHash(uInt256);
        }
        return blockHeader;
    }

    //mo44447c
    //910 mo44528c
    public UInt256 getBlockHashFromDb(long no) {
        if (no < 0 || no > ((long) getCurrentBlockNo())) {
            return null;
        }
        return this.blockTable.selectBlockHashByNo(no);
    }

    //m12830b
    private BlockInfo getBlockInfoFromBlockHeader(BlockHeader blockHeader, boolean... zArr) {
        if (blockHeader == null) {
            return null;
        }
        BlockInfo blockInfo = this.wallet.getSelfWalletHelper().getBlockInfoFromBlockHeader(blockHeader);
        if (zArr.length != 0 && zArr[0]) {
            blockInfo.setTransactionList(this.wallet.getSelfTransactionModel().getConfirmedTransactionListFromAll(blockInfo.getBlockHash()), new boolean[0]);
        }
        return blockInfo;
    }

    //mo44446b
    public boolean isBlockHashInCacheOrDb(UInt256 uInt256, boolean... ingoreBlockTable) {
        if (this.blockHashToBlockHeaderCacheMap.containsKey(uInt256)) {
            return true;
        }
        if (ingoreBlockTable.length <= 0 || ingoreBlockTable[0]) {
            return false;
        }
        return this.blockTable.isBlockHashInDbTable(uInt256);
    }

    //m12832c
    private void initCurBlockHeaderAndSyncStatus(BlockHeader blockHeader) {
        BlockHeader tempBlockHeader = this.mCurBlockHeader;
        if ((tempBlockHeader == null || tempBlockHeader.getBlockHash().isNull()) && blockHeader.isFirstBlock()) {
            this.mCurBlockHeader = blockHeader;
        }
        if (this.maxBlockNo < ((long) blockHeader.getBlockNo())) {
            this.maxBlockNo = (long) blockHeader.getBlockNo();
        }
        if (this.blockIndex == null) {
            this.blockIndex = new BlockIndex(blockHeader);
        } else if (blockHeader.getBlockNo() >= this.blockIndex.blockNo) {
            this.blockIndex.initDataFromBlockHeader(blockHeader);
        } else if (blockHeader.getPreBlockHash().equals(this.blockIndex.blockHash)) {
            if (blockHeader.isFirstBlock()) {
                blockHeader.setBlockNo(0);
            } else {
                blockHeader.setBlockNo(this.blockIndex.blockNo + 1);
            }
            this.blockIndex.initDataFromBlockHeader(blockHeader);
        } else if (blockHeader.getBlockHash().equals(this.blockIndex.blockHash)) {
            this.blockIndex.blockSyncStatus = blockHeader.getBlockSyncStatus();
        }
        if (blockHeader.getBlockSyncStatus() == BlockSyncStatus.SYNCHED) {
            BlockHeader header = this.mCurBlockHeader;
            if (header == null || header.getBlockNo() <= blockHeader.getBlockNo()) {
                this.mCurBlockHeader = blockHeader;
            }
        }
        addToHashBlockHeaderCacheMap(blockHeader);
    }

    /* access modifiers changed from: protected */
    //mo44430a
    //910 mo44512a
    public void addToHashBlockHeaderCacheMap(BlockHeader blockHeader) {
        BlockHeader temBlockHeader = (BlockHeader) this.blockHashToBlockHeaderCacheMap.get(blockHeader.getBlockHash());
        if (temBlockHeader != blockHeader) {
            if (temBlockHeader == null) {
                if (this.blockHashToBlockHeaderCacheMap.size() > 1000) {
                    LinkedHashMap<UInt256, BlockHeader> linkedHashMap = this.blockHashToBlockHeaderCacheMap;
                    linkedHashMap.remove(((BlockHeader) linkedHashMap.values().iterator().next()).getBlockHash());
                }
                this.blockHashToBlockHeaderCacheMap.put(new UInt256((BaseBlob) blockHeader.getBlockHash()), blockHeader);
            } else {
                temBlockHeader.initFromBlockHeader(blockHeader);
            }
        }
    }

    //mo44445b
    //910 mo44537d
    public boolean isNewestBlock() {
        BlockHeader blockHeader = this.mCurBlockHeader;
        boolean z = false;
        if (blockHeader == null || blockHeader.getBlockHash().equals(this.wallet.getChainParams().hash)) {
            return false;
        }
        if (((long) this.mCurBlockHeader.getBlockNo()) == this.maxBlockNo) {
            z = true;
        }
        return z;
    }

    //mo44448c
    public void fillConfirmedBlockHashToTxidsMap() {
        this.confirmedBlockHashToTxidsMap.clear();
        List<Transaction> transactionList = this.wallet.getSelfTransactionModel().getConfirmedAndUnConfirmTransactionList();
        if (transactionList != null && !transactionList.isEmpty()) {
            for (Transaction transaction : transactionList) {
                UInt256 blockHash = transaction.getBlockHash();
                if (blockHash != null && !blockHash.isNull()) {
                    List<UInt256> list =this.confirmedBlockHashToTxidsMap.get(blockHash);
                    if (list == null) {
                        list = new ArrayList<UInt256>();
                        this.confirmedBlockHashToTxidsMap.put(new UInt256((BaseBlob) blockHash), list);
                    }
                    list.add(transaction.getTxId());
                }
            }
        }
    }

    /* renamed from: s */
    private void m12834s() {
        try {
            BlockInfo blockInfo = this.wallet.getChainParams().getFirstBlockInfo();
            blockInfo.setBlockHeaderSynchronized(BlockSyncStatus.SYNCHED);
            m12831b(blockInfo);
            this.mCurBlockHeader = blockInfo.getBlockHeader();
            this.blockIndex.initDataFromBlockHeader(this.mCurBlockHeader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //910 mo42738c
    public void mo42680c(UInt256 blockHash) throws InvalidateBlockException {
        SparseArray accountSpareArray = new SparseArray();
        HashMap hashMap = new HashMap();
        ArrayList transactionList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        BlockHeader blockHeader = getBlockHeaderFromCachedAndDbByHash(blockHash);
        if (blockHeader.getBlockNo() < this.blockIndex.blockNo) {
            this.blockTable.beginTransaction();
            mo44426a(checkAndGetBlockHeaderFromDb((long) (blockHeader.getBlockNo() + 1)).getBlockHash(), (List<BlockHeader>) arrayList2, hashMap, accountSpareArray, (List<Transaction>) transactionList);
            this.blockTable.endTransaction(true);
            calAllSubAddressBalanceInfo(hashMap.values());
            mo44450c(accountSpareArray.getVector());
            BlockChainModelEvent chainModelEvent = this.mBlockChainModelEvent;
            if (chainModelEvent != null) {
                chainModelEvent.mo44063a((List<BlockHeader>) arrayList2, (List<Transaction>) transactionList, hashMap);
            }
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo44426a(UInt256 uInt256, List<BlockHeader> list, HashMap<CTxDestination, Address> relatedAddressMap, SparseArray<Account> relatedAddressArray, List<Transaction> list2) throws InvalidateBlockException {
        TransactionModel transactionModel = this.wallet.getSelfTransactionModel();
        BlockHeader prevBlockHeader = getBlockHeaderFromCachedAndDbByHash(getBlockHeaderFromCachedAndDbByHash(uInt256).getPreBlockHash());
        Vector relatedDesList = new Vector();
        TransactionDB transactionDB = this.wallet.getSelfTransactionDB();
        transactionDB.beginTransaction();
        while (this.blockIndex.blockNo > prevBlockHeader.getBlockNo()) {
            try {
                relatedDesList.clear();
                BlockInfo blockInfo = checkAndGetBlockInfoByBlockHash(this.blockIndex.blockHash, true);
                list.add(blockInfo.getBlockHeader());
                this.blockHashToBlockHeaderCacheMap.remove(blockInfo.getBlockHash());
                List<Transaction> confirmedTransactionList = transactionModel.getConfirmedTransactionListFromAll(blockInfo.getBlockHash());
                this.confirmedBlockHashToTxidsMap.remove(blockInfo.getBlockHash());
                if (confirmedTransactionList != null) {
                    for (Transaction confimedTransaction : confirmedTransactionList) {
                        transactionModel.mo44507b(confimedTransaction, list2);
                        confimedTransaction.fillDesListWithRelatedAddress((List<CTxDestination>) relatedDesList);
                        Iterator it = relatedDesList.iterator();
                        while (it.hasNext()) {
                            findRelatedAddressAndAccountToMapByDes((CTxDestination) it.next(), relatedAddressMap, relatedAddressArray);
                        }
                    }
                }
                mo42678a(blockInfo);
                this.blockTable.deleteDataByOtherTableItemSynchronized((AbstractTableItem) blockInfo);
                BlockHeader curBlockIndexPreBlockHeader = getBlockHeaderFromCachedAndDbByHash(blockInfo.getPreBlockHash());
                if (this.mCurBlockHeader.getBlockNo() > curBlockIndexPreBlockHeader.getBlockNo()) {
                    this.mCurBlockHeader.initFromBlockHeader(curBlockIndexPreBlockHeader);
                }
                this.blockIndex.initDataFromBlockHeader(curBlockIndexPreBlockHeader);
                this.maxBlockNo = (long) curBlockIndexPreBlockHeader.getBlockNo();
                transactionDB.endTransaction(true);
            } catch (InvalidateBlockException e) {
                e.printStackTrace();
                transactionDB.endTransaction(false);
                throw e;
            } catch (Exception e2) {
                e2.printStackTrace();
                transactionDB.endTransaction(false);
                return;
            }
        }
    }

    //获得当前同步的区块哈希往前10个，再往前间隔每个乘2
    //mo44453d
    public List<byte[]> getLastNumberBlockHashList() throws Throwable {
        ArrayList<byte[]> arrayList = new ArrayList<>();
        ChainParams chainParams = this.wallet.getChainParams();
        BlockHeader blockHeader = this.mCurBlockHeader;
        if (blockHeader == null) {
            arrayList.add(chainParams.hash.data());
            return arrayList;
        }
        int height = blockHeader.getBlockNo();
        ArrayList<Integer> arrayList2 = new ArrayList<>();
        int interval = 1;
        for (int i = 0; i < height; i += interval) {
            arrayList2.add(height - i);
            if (arrayList2.size() >= 10) {
                interval *= 2;
            }
        }
        arrayList.addAll(this.blockTable.getBlockHashListFromHeightList(arrayList2));
        if (arrayList.isEmpty() || !Arrays.equals((byte[]) arrayList.get(arrayList.size() - 1), chainParams.hash.data())) {
            arrayList.add(chainParams.hash.data());
        }
        return arrayList;
    }


    public List<byte[]> mo44496a(List<CachedBlockInfo> list) {
        ArrayList arrayList = new ArrayList();
        ChainParams chainParams = this.wallet.getChainParams();
        int size = list.size();
        ArrayList<Integer> arrayList2 = new ArrayList<>();
        int i = 1;
        for (int i2 = 1; i2 < size; i2 += i) {
            arrayList2.add(Integer.valueOf(size - i2));
            if (arrayList2.size() >= 10) {
                i *= 2;
            }
        }
        for (Integer intValue : arrayList2) {
            arrayList.add(((CachedBlockInfo) list.get(intValue.intValue())).blockHeader.getBlockHash().data());
        }
        if (arrayList.isEmpty() || !Arrays.equals((byte[]) arrayList.get(arrayList.size() - 1), chainParams.hash.data())) {
            arrayList.add(chainParams.hash.data());
        }
        return arrayList;
    }

    //mo44417a
    public List<byte[]> getIntervalBlockHashListFromCached(Collection<CachedBlockInfo> collection) {
        ArrayList arrayList = new ArrayList();
        ChainParams chainParams = this.wallet.getChainParams();
        Iterator it = collection.iterator();
        int i = 0;
        int i2 = 1;
        while (it.hasNext()) {
            arrayList.add(((CachedBlockInfo) it.next()).blockHeader.getBlockHash().data());
            i++;
            if (i >= 10) {
                i2 *= 2;
            }
            int i3 = 0;
            while (i3 < i2 && it.hasNext()) {
                i3++;
                it.next();
            }
        }
        if (arrayList.isEmpty() || !Arrays.equals((byte[]) arrayList.get(arrayList.size() - 1), chainParams.hash.data())) {
            arrayList.add(chainParams.hash.data());
        }
        return arrayList;
    }

    //mo44458e
    public BlockHeader getNewestBlockHeader() {
        return this.mCurBlockHeader;
    }

    //mo44460f
    //910 mo44544i
    public BlockInfo getCurBlockInfo() {
        BlockHeader blockHeader = this.mCurBlockHeader;
        if (blockHeader == null) {
            return null;
        }
        return checkAndGetBlockInfoByBlockHash(blockHeader.getBlockHash(), new boolean[0]);
    }

    //mo44418a
    public List<BlockHeader> getBlockHeaderFromCurBlockNoAndLimit1000(int... iArr) {
        int limit = 1000;
        if (iArr.length > 0 && iArr[0] > 0 && iArr[0] < 1000) {
            limit = iArr[0];
        }
        return this.blockTable.getBlockHeaderByMinNoAndLimit(this.mCurBlockHeader.getBlockNo() + 1, limit);
    }

    //mo44462h
    public BlockIndex getBlockIndex() {
        return this.blockIndex;
    }

    //mo44463i
    public ChainSyncStatus getChainSyncStatus() {
        return this.mChainSyncStatus;
    }

    /* access modifiers changed from: protected */
    //mo44429a
    //910 mo44509a
    public void changeChainSyncStatus(ChainSyncStatus chainSyncStatus) {
        if (this.mChainSyncStatus != chainSyncStatus) {
            StringBuilder sb = new StringBuilder();
            sb.append("Block chain sync status changed: ");
            sb.append(chainSyncStatus);
            Log.m11473a((Object) this, sb.toString());
            this.mChainSyncStatus = chainSyncStatus;
            notifBlockNoUpdate();
        }
    }

    //mo44464j
    public void checkNewestBlockAndChangeStatus() {
        if (isNewestBlock()) {
            changeChainSyncStatus(ChainSyncStatus.SYNCHED);
        } else {
            changeChainSyncStatus(ChainSyncStatus.SYNCHING);
        }
    }

    //mo44465k
    //910 mo44548m
    public long getMaxBlockNo() {
        return this.maxBlockNo;
    }

    /* renamed from: b */
    private boolean m12831b(BlockInfo blockInfo) throws InvalidateBlockException {
        setNChainWorkFromPreBlockInfo(blockInfo.getBlockHeader());
        long blockTime = blockInfo.getBlockTime();
        List<Transaction> transactionList = blockInfo.getSelfTransactionList();
        if (transactionList != null) {
            for (Transaction transaction : transactionList) {
                transaction.setTimeStamp(blockTime);
            }
        }
        return mo42679a((CachedBlockInfo) null, blockInfo, new HashMap<>(), new SparseArray<>());
    }

    //mo44437a
    //910 mo44527b
    public boolean addBlockListToTable(List<CachedBlockInfo> cachedBlockInfoList) throws InvalidateBlockException {
        if (cachedBlockInfoList.isEmpty()) {
            Log.logObjectWarning((Object) this, "Could not add empty block list to chain.");
            return false;
        }
        SparseArray accountSpareseArr = new SparseArray();
        HashMap hashMap = new HashMap();
        this.wallet.getSelfTransactionModel().mo44509b(new LinkedList<Transaction>());
        this.blockTable.beginTransaction();
        try {
            ArrayList<CachedBlockInfo> waitAddCachedBlockList = new ArrayList();
            for (CachedBlockInfo cachedBlockInfo : cachedBlockInfoList) {
                if (cachedBlockInfo.txidToTransactionLinkedMap != null) {
                    if (!cachedBlockInfo.txidToTransactionLinkedMap.isEmpty()) {
                        checkCurBlockHeaderAndReplaceBlock((List<CachedBlockInfo>) waitAddCachedBlockList);
                        waitAddCachedBlockList.clear();
                        if (this.mCurBlockHeader.getBlockNo() + 1 < cachedBlockInfo.blockHeader.getBlockNo()) {
                            mo44449c(cachedBlockInfo.blockHeader.getBlockNo());
                        }
                        checkAndReplacSaplingBlock(cachedBlockInfo);
                        mo42679a(cachedBlockInfo, cachedBlockInfo.getBlockInfoAndSetTransactionList(this.wallet), hashMap, accountSpareseArr);
                    }
                }
                waitAddCachedBlockList.add(cachedBlockInfo);
            }
            checkCurBlockHeaderAndReplaceBlock((List<CachedBlockInfo>) waitAddCachedBlockList);
            this.blockTable.endTransaction(true);
            checkNewestBlockAndChangeStatus();
            if (!hashMap.isEmpty()) {
                calAllSubAddressBalanceInfo(hashMap.values());
                mo44450c(accountSpareseArr.getVector());
            }
            return true;
        } catch (Exception e) {
            this.blockTable.endTransaction(false);
            e.printStackTrace();
            throw e;
        }
    }

    /* access modifiers changed from: protected */
    //mo44443b
    public void checkCurBlockHeaderAndReplaceBlock(List<CachedBlockInfo> list) {
        if (!list.isEmpty()) {
            ArrayList<CachedBlockInfo> addCachedBlockList = new ArrayList<CachedBlockInfo>();
            int lastUsynchedNo = -1;
            for (CachedBlockInfo cachedBlockInfo : list) {
                checkAndReplacSaplingBlock(cachedBlockInfo);
                BlockHeader blockHeader = getBlockHeaderFromCachedAndDbByHash(cachedBlockInfo.blockHeader.getBlockHash());
                if (blockHeader == null) {
                    if (lastUsynchedNo != -1) {
                        getBlockHeaderAndCompareSetCurBlockHeader(lastUsynchedNo);
                        lastUsynchedNo = -1;
                    }
                    addCachedBlockList.add(cachedBlockInfo);
                } else {
                    if (!addCachedBlockList.isEmpty()) {
                        replaceCachedBlockInfoList((List<CachedBlockInfo>) addCachedBlockList);
                        addCachedBlockList.clear();
                    }
                    if (blockHeader.getBlockSyncStatus() != BlockSyncStatus.SYNCHED) {
                        lastUsynchedNo = blockHeader.getBlockNo();
                    }
                }
            }
            if (!addCachedBlockList.isEmpty()) {
                replaceCachedBlockInfoList((List<CachedBlockInfo>) addCachedBlockList);
            } else if (lastUsynchedNo > -1) {
                getBlockHeaderAndCompareSetCurBlockHeader(lastUsynchedNo);
            }
            list.clear();
        }
    }

    /* access modifiers changed from: protected */
    //910 mo44507a
    public void mo44427a(Transaction transaction, List<CTxDestination> txDestinationList, HashMap<CTxDestination, Address> hashMap, SparseArray<Account> accountSparseArray, boolean needReCompute) {
        txDestinationList.clear();
        if (needReCompute) {
            transaction.recomputeByTxs(txDestinationList);
        } else {
            transaction.fillDesListWithRelatedAddress(txDestinationList);
        }
        if (!txDestinationList.isEmpty()) {
            for (CTxDestination des : txDestinationList) {
                findRelatedAddressAndAccountToMapByDes(des, hashMap, accountSparseArray);
            }
        }
    }

    //mo44444b
    public void setNChainWorkFromPreBlockInfo(BlockHeader blockHeader) {
        setNChainWorkFromOtherBlockInfo(checkAndGetBlockInfoByBlockHash(blockHeader.getPreBlockHash(), new boolean[0]), blockHeader);
    }

    //mo44433a
    public void setNChainWorkFromOtherBlockInfo(BlockInfo preBlockInfo, BlockHeader blockHeader) {
        blockHeader.setNChainWork((preBlockInfo != null ? preBlockInfo.getBlockHeader().getNChainWork() : new ArithUint256(0)).mo9499b(bitcoin.Pow.m447a(blockHeader)));
    }

    //mo44431a
    //910 mo44513a
    public void copyFirstBlockHeaderToSecond(BlockHeader blockHeader, BlockHeader blockHeader1) {
        blockHeader1.setNChainWork((blockHeader != null ? blockHeader.getNChainWork() : new ArithUint256(0)).mo9499b(bitcoin.Pow.m447a(blockHeader1)));
    }

    //910 mo44508a
    public void mo44428a(ArrayListMap<UInt256, Transaction> waitConfirmedTransactionMap, List<Transaction> list, SparseArray<Account> gtVar, HashMap<CTxDestination, Address> hashMap) {
        List<Transaction> waitConfirmedTransactionList = waitConfirmedTransactionMap.getValueList();
        ArrayList<CTxDestination> arrayList = new ArrayList<>();
        int size = list.size();
        TransactionModel transactionModel = this.wallet.getSelfTransactionModel();
        for (Transaction waitConfirmedTransaction : waitConfirmedTransactionList) {
            for (TxIn txIn : waitConfirmedTransaction.getSelfTxInList()) {
                List<Transaction> unConfirmedTransactionList = transactionModel.getUnConfirmTransactionFromTxIn(txIn);
                if (unConfirmedTransactionList != null && !unConfirmedTransactionList.isEmpty()) {
                    for (Transaction unConfirmTransation : unConfirmedTransactionList) {
                        list.add(unConfirmTransation);
                        transactionModel.mo44507b(unConfirmTransation, list);
                    }
                }
            }
        }
        if (size != list.size()) {
            ListIterator listIterator = list.listIterator(size);
            while (listIterator.hasNext()) {
                ((Transaction) listIterator.next()).fillDesListWithRelatedAddress((List<CTxDestination>) arrayList);
                for (CTxDestination a4 : arrayList) {
                    findRelatedAddressAndAccountToMapByDes(a4, hashMap, gtVar);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    //mo44442b
    public void calAllSubAddressBalanceInfo(Collection<Address> addressCollection) {
        for (Address address : addressCollection) {
            address.calSubAddressBalanceInfo(this.wallet.getBlockChainType());
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: c */
    public void mo44450c(Collection<Account> collection) {
        for (Account a : collection) {
            a.calSubAccountBalanceInfo(false, this.wallet.getBlockChainType());
        }
    }

    /* access modifiers changed from: protected */
    //910 mo44517a
    //mo44435a
    public void findRelatedAddressAndAccountToMapByDes(CTxDestination des, HashMap<CTxDestination, Address> desToAddressMap, SparseArray<Account> accountSparseArray) {
        if (des != null && !des.isNull() && !desToAddressMap.containsKey(des)) {
            AddressModel addressModel = this.wallet.getSelfAddressModel();
            if (!m12829a(addressModel, des, desToAddressMap, accountSparseArray)) {
                Address address = addressModel.getAddressByCTxDestinationFromUsingAddressMap(des);
                if (address == null) {
                    address = addressModel.getAddressFromUnuseAddressMap(des);
                }
                if (address == null) {
                    address = addressModel.getAddressFromUsingAddressShadowMap(des);
                }
                if (address != null) {
                    desToAddressMap.put(des, address);
                    if (!accountSparseArray.isNotNull(address.getAccount())) {
                        Account account = this.wallet.getAccountFromSparseArr(address.getAccount());
                        if (account != null) {
                            accountSparseArray.put(address.getAccount(), account);
                        }
                    }
                }
            }
        }
    }

    /* renamed from: a */
    private boolean m12829a(AddressModel addressModel, CTxDestination des, HashMap<CTxDestination, Address> desToAddressMap, SparseArray<Account> accountSparseArray) {
        Address address = addressModel.mo43116j(des);
        if (address == null) {
            return false;
        }
        desToAddressMap.put(des, address);
        accountSparseArray.put(address.getAccount(), this.wallet.getSelfAccountModel().getAccountFromSparseArr(address.getAccount()));
        return false;
    }

    /* access modifiers changed from: protected */
    //mo44438a
    //910 mo44519a
    public boolean checkAndAddNewBlock(BlockHeader blockHeader, boolean... zArr) {
        if (!blockHeader.isFirstBlock() || !this.blockIndex.blockHash.isNull()) {
            BlockHeader preBlockHeader = getBlockHeaderFromCachedAndDbByHash(blockHeader.getPreBlockHash());
            if (preBlockHeader == null) {
                return false;
            }
            blockHeader.setBlockNo(preBlockHeader.getBlockNo() + 1);
            initCurBlockHeaderAndSyncStatus(blockHeader);
            if (this.maxBlockNo < ((long) blockHeader.getBlockNo())) {
                this.maxBlockNo = (long) blockHeader.getBlockNo();
            }
            if (zArr.length > 0 && zArr[0]) {
                StringBuilder sb = new StringBuilder();
                sb.append("add block ");
                sb.append(blockHeader.getBlockNo());
                sb.append(" --> ");
                sb.append(blockHeader.getBlockHash());
                Log.info("block_test", sb.toString());
                this.blockTable.replace((AbstractTableItem) blockHeader);
            }
            checkNewestBlockAndChangeStatus();
            return true;
        }
        this.blockTable.replace((AbstractTableItem) blockHeader);
        initCurBlockHeaderAndSyncStatus(blockHeader);
        return true;
    }

    //
    //mo44451c
    public void replaceCachedBlockInfoList(List<CachedBlockInfo> list) {
        Vector vector = new Vector(0);
        HashMap hashMap = new HashMap(0);
        CachedBlockInfo maxBlockInfo=null;
        maxBlockInfo=list.get(0);
        for (CachedBlockInfo cachedBlockInfo : list) {
            cachedBlockInfo.blockHeader.setBlockSyncStatus(BlockSyncStatus.SYNCHED);
            if (!cachedBlockInfo.blockHeader.isFirstBlock() || !this.blockIndex.blockHash.isNull()) {
                BlockHeader preBlockHeader = getBlockHeaderFromCachedAndDbByHash(cachedBlockInfo.blockHeader.getPreBlockHash());
                if (preBlockHeader == null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Could not found prev header ");
                    sb.append(cachedBlockInfo.blockHeader.getPreBlockHash());
                    sb.append(" for ");
                    sb.append(cachedBlockInfo.blockHeader.getBlockHash());
                    Log.LogObjError((Object) this, sb.toString());
                } else if (preBlockHeader.getBlockSyncStatus() != BlockSyncStatus.SYNCHED) {
                    preBlockHeader.setBlockSyncStatus(BlockSyncStatus.SYNCHED);
                    this.blockTable.updateBlockStatus(preBlockHeader);
                    notifyUpdateBlock(this.wallet.getSelfWalletHelper().getBlockInfoFromBlockHeader(preBlockHeader), (List<Transaction>) vector, hashMap);
                }
                cachedBlockInfo.blockHeader.setBlockNo(preBlockHeader.getBlockNo() + 1);
                this.blockTable.replace((AbstractTableItem) cachedBlockInfo.blockHeader);
                notifyUpdateBlock(this.wallet.getSelfWalletHelper().getBlockInfoFromBlockHeader(cachedBlockInfo.blockHeader), (List<Transaction>) vector, hashMap);
                addToHashBlockHeaderCacheMap(cachedBlockInfo.blockHeader);
            } else {
                cachedBlockInfo.blockHeader.setBlockNo(0);
                this.blockTable.replace((AbstractTableItem) cachedBlockInfo.blockHeader);
                notifyUpdateBlock(this.wallet.getSelfWalletHelper().getBlockInfoFromBlockHeader(cachedBlockInfo.blockHeader), (List<Transaction>) vector, hashMap);
            }
            try {
                findRelatedSaplingTransactionAndUpdateVTxSapn(cachedBlockInfo, getWalllet().getSelfWalletHelper().getBlockInfoFromBlockHeader(cachedBlockInfo.blockHeader));
                if (this.maxBlockNo < ((long) cachedBlockInfo.blockHeader.getBlockNo())) {
                    this.maxBlockNo = (long) cachedBlockInfo.blockHeader.getBlockNo();
                }
                if(cachedBlockInfo.blockHeader.getBlockNo()>maxBlockInfo.blockHeader.getBlockNo()){
                    maxBlockInfo=cachedBlockInfo;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.LogObjError((Object) this, "Failed to connect tip for empty block", (Throwable) e);
                return;
            }
        }
        if (this.blockIndex.blockNo <= maxBlockInfo.blockHeader.getBlockNo()) {
            this.blockIndex.initDataFromBlockHeader(maxBlockInfo.blockHeader);
        }
        if (this.mCurBlockHeader.getBlockNo() <= maxBlockInfo.blockHeader.getBlockNo()) {
            this.mCurBlockHeader = maxBlockInfo.blockHeader;
        }
        if (this.maxBlockNo < ((long) maxBlockInfo.blockHeader.getBlockNo())) {
            this.maxBlockNo = (long) maxBlockInfo.blockHeader.getBlockNo();
        }
        notifBlockNoUpdate();
    }

    /* access modifiers changed from: protected */
    /* renamed from: d */
    public void mo44456d(Collection<BlockHeader> collection) {
        for (BlockHeader blockHeader : collection) {
            initCurBlockHeaderAndSyncStatus(blockHeader);
        }
    }

    //mo44425a
    public synchronized void deleteBlocksSinceStartBlockNo(long startBlockNo, boolean z) {
        if (startBlockNo < 1) {
            startBlockNo = 1;
        }
        this.blockTable.deleteBlocksSinceStartBlockNo(startBlockNo, BlockSyncStatus.SYNCING);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0073, code lost:
        return;
     */
    /* renamed from: a */
    public synchronized void mo44422a(int i) {
        if (i < this.mCurBlockHeader.getBlockNo()) {
            long j = (long) i;
            if (getBlockInfoFromDb(j, new boolean[0]) != null) {
                this.mCurBlockHeader = getBlockInfoFromDb(j, new boolean[0]).getBlockHeader();
                for (Map.Entry value : this.blockHashToBlockHeaderCacheMap.entrySet()) {
                    ((BlockHeader) value.getValue()).setBlockSyncStatus(BlockSyncStatus.SYNCING);
                }
                this.blockTable.mo44341a(j, BlockSyncStatus.SYNCING);
                changeChainSyncStatus(ChainSyncStatus.SYNCHING);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("block not found");
                sb.append(i);
                sb.append(" , ");
                sb.append(this.mCurBlockHeader.getBlockHash());
                Log.infoObject((Object) this, sb.toString());
            }
        }
    }

    //mo44441b
    //910 mo44523b
    public void setMaxBlockNoNotForce(int i) {
        setMaxBlockNo(i, false);
    }

    //mo44424a
    //910 mo44503a
    public void setMaxBlockNo(int i, boolean force) {
        long j = (long) i;
        if (this.maxBlockNo < j || force) {
            this.maxBlockNo = j;
            checkNewestBlockAndChangeStatus();
            notifBlockNoUpdate();
        }
    }

    //mo44454d
    public List<UInt256> getConfirmedTxids(UInt256 blockHash) {
        return (List) this.confirmedBlockHashToTxidsMap.get(blockHash);
    }

    //mo44466l
    public void initDbAndTable() {
        if (this.blockTable == null) {
            this.blockTable = this.wallet.getSelfTransactionDB().getSelfBlockTable();
        }
        if (this.blockTable.getDbWritableDataHelper().isOpen()) {
            m12834s();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0040, code lost:
        if (mo44445b() != false) goto L_0x005b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0059, code lost:
        if (mo44445b() == false) goto L_0x0060;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x005b, code lost:
        r4.f13197b = p000.ChainSyncStatus.f12954c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0060, code lost:
        r4.f13197b = p000.ChainSyncStatus.f12953b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        return;
     */
    /* renamed from: g */
    public void initAllDataFromDb() {
        this.blockTable.beginTransaction();
        try {
            List<BlockHeader> synchedBlockList = this.blockTable.selectSynchedBlockByLimit((int) HttpStatus.HTTP_INTERNAL_SERVER_ERROR);
            mo44456d((Collection<BlockHeader>) synchedBlockList);
            if (synchedBlockList.size() < 500) {
                mo44456d((Collection<BlockHeader>) this.blockTable.mo44352e(HttpStatus.HTTP_INTERNAL_SERVER_ERROR - synchedBlockList.size()));
            }
            this.blockIndex.initDataFromBlockHeader(this.blockTable.getBlockHeaderByNo());
            this.blockTable.endTransaction(false);
            if (this.mCurBlockHeader == null) {
                m12834s();
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.blockTable.endTransaction(false);
            if (this.mCurBlockHeader == null) {
                m12834s();
            }
        } catch (Throwable th) {
            this.blockTable.endTransaction(false);
            if (this.mCurBlockHeader == null) {
                m12834s();
            }
            if (isNewestBlock()) {
                this.mChainSyncStatus = ChainSyncStatus.SYNCHED;
            } else {
                this.mChainSyncStatus = ChainSyncStatus.SYNCHING;
            }
            throw th;
        }
    }

    /* renamed from: c */
    public void mo44449c(int i) {
        BlockHeader headerFromDb = checkAndGetBlockHeaderFromDb((long) i);
        if (headerFromDb != null) {
            mo44459e(headerFromDb.getBlockHash());
        }
    }

    /* renamed from: e */
    public synchronized void mo44459e(UInt256 blockHash) {
        if (!blockHash.equals(this.mCurBlockHeader.getBlockHash())) {
            BlockHeader blockHeader = getBlockHeaderFromCachedAndDbByHash(blockHash);
            if (blockHeader != null) {
                if (blockHeader.getBlockNo() >= this.mCurBlockHeader.getBlockNo()) {
                    if (blockHeader.getBlockNo() != 0) {
                        BlockHeader preBlockHeader = getBlockHeaderFromCachedAndDbByHash(blockHeader.getPreBlockHash());
                        this.mCurBlockHeader = preBlockHeader;
                        this.mCurBlockHeader.setBlockSyncStatus(BlockSyncStatus.SYNCHED);
                        this.blockTable.updateBlockSyncStatusByNo(preBlockHeader.getBlockNo(), BlockSyncStatus.SYNCHED);
                        setSmallerCurBlockNoSynchedAndClearCacheMap(preBlockHeader);
                        if (this.blockHashToBlockHeaderCacheMap.isEmpty()) {
                            this.blockHashToBlockHeaderCacheMap.put(new UInt256((BaseBlob) this.mCurBlockHeader.getBlockHash()), this.mCurBlockHeader);
                        }
                        checkNewestBlockAndChangeStatus();
                        notifyUpdateBlock(getBlockInfoFromBlockHeader(blockHeader, false), null, null);
                    }
                }
            }
        }
    }

    //mo44455d
    public void getBlockHeaderAndCompareSetCurBlockHeader(int no) {
        BlockHeader blockHeader = checkAndGetBlockHeaderFromDb((long) no);
        if (blockHeader != null) {
            compareAndSetCurBlockHeader(blockHeader.getBlockHash());
        }
    }

    //mo44461f
    public synchronized void compareAndSetCurBlockHeader(UInt256 blockHash) {
        if (!blockHash.equals(this.mCurBlockHeader.getBlockHash())) {
            BlockHeader blockHeader = getBlockHeaderFromCachedAndDbByHash(blockHash);
            if (blockHeader != null) {
                if (blockHeader.getBlockNo() >= this.mCurBlockHeader.getBlockNo()) {
                    if (blockHeader.getBlockNo() != 0) {
                        int originalBlockNo = this.mCurBlockHeader.getBlockNo();
                        this.mCurBlockHeader = blockHeader;
                        blockHeader.setBlockSyncStatus(BlockSyncStatus.SYNCHED);
                        notifyOnCurBlockChange(originalBlockNo, blockHeader.getBlockNo(), (int) this.maxBlockNo);
                        this.blockTable.updateBlockSyncStatusByNo(blockHeader.getBlockNo(), BlockSyncStatus.SYNCHED);
                        int originalNextBlockNo = originalBlockNo + 1;
                        while (originalNextBlockNo <= blockHeader.getBlockNo()) {
                            try {
                                findRelatedSaplingTransactionAndUpdateVTxSapn((CachedBlockInfo) null, getBlockInfoFromDb((long) originalNextBlockNo, false));
                                originalNextBlockNo++;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        setSmallerCurBlockNoSynchedAndClearCacheMap(blockHeader);
                        checkNewestBlockAndChangeStatus();
                        notifyUpdateBlock(getBlockInfoFromBlockHeader(blockHeader, false), null, null);
                    }
                }
            }
        }
    }

    //m12833d
    private void setSmallerCurBlockNoSynchedAndClearCacheMap(BlockHeader blockHeader) {
        BlockHeader preBlockHeader = getBlockHeaderFromCachedAndDbByHash(blockHeader.getPreBlockHash());
        int blockNo = blockHeader.getBlockNo();
        if (preBlockHeader != null) {
            Set<Map.Entry<UInt256,BlockHeader>> entrySet = this.blockHashToBlockHeaderCacheMap.entrySet();
            for (Map.Entry value : entrySet) {
                BlockHeader tempBlockHeader = (BlockHeader) value.getValue();
                if (tempBlockHeader.getBlockNo() <= blockNo) {
                    if (tempBlockHeader.getBlockSyncStatus() == BlockSyncStatus.SYNCHED) {
                        break;
                    }
                    tempBlockHeader.setBlockSyncStatus(BlockSyncStatus.SYNCHED);
                }
            }
            if (this.blockHashToBlockHeaderCacheMap.size() > 1000) {
                BlockHeader cacheMapFirstBlockHeader = (BlockHeader) ((Map.Entry) entrySet.iterator().next()).getValue();
                for (int size = this.blockHashToBlockHeaderCacheMap.size() - 1000; size > 0 && cacheMapFirstBlockHeader != null; size--) {
                    this.blockHashToBlockHeaderCacheMap.remove(cacheMapFirstBlockHeader.getBlockHash());
                }
            }
        }
        if (this.blockHashToBlockHeaderCacheMap.isEmpty()) {
            this.blockHashToBlockHeaderCacheMap.put(new UInt256((BaseBlob) this.mCurBlockHeader.getBlockHash()), this.mCurBlockHeader);
        }
    }

    //mo44467m
    public void clearAll() {
        this.maxBlockNo = 0;
        this.mCurBlockHeader = null;
        this.blockIndex.clear();
        this.blockHashToBlockHeaderCacheMap.clear();
        this.confirmedBlockHashToTxidsMap.clear();
    }

    //mo44457d
    //910 mo44540e
    public void notifyOnTransactionsConfirmed(List<Transaction> list) {
        BlockChainModelEvent chainModelEvent = this.mBlockChainModelEvent;
        if (chainModelEvent != null) {
            chainModelEvent.notifyOnTransactionsConfirmed(list);
        }
    }

    /* access modifiers changed from: protected */
    //mo44432a
    //910 mo44514a
    public void notifyUpdateBlock(BlockInfo juVar, List<Transaction> list, HashMap<CTxDestination, Address> hashMap) {
        BlockChainModelEvent event = this.mBlockChainModelEvent;
        if (event != null) {
            event.onUpdateBlock(juVar, list, hashMap);
        }
    }

    /* access modifiers changed from: protected */
    //mo44423a
    public void notifyOnCurBlockChange(int pre, int curNo, int maxNo) {
        BlockChainModelEvent chainModelEvent = this.mBlockChainModelEvent;
        if (chainModelEvent != null) {
            chainModelEvent.onCurBlockNoChange(pre, curNo, maxNo);
        }
    }

    /* access modifiers changed from: protected */
    //mo44468n
    public void notifBlockNoUpdate() {
        BlockChainModelEvent event = this.mBlockChainModelEvent;
        if (event != null) {
            event.onBlockNoUpdate(getNewestBlockHeader(), (int) getMaxBlockNo(), this.mChainSyncStatus);
        }
    }

    //mo44452d
    public long getTimeStampByBlockNo(long blockNo) {
        return this.blockTable.getTimeStampByBlockNo(blockNo);
    }
}

