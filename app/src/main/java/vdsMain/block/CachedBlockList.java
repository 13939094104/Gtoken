package vdsMain.block;

import bitcoin.BaseBlob;
import bitcoin.UInt256;
import vdsMain.CTxDestination;
import vdsMain.Pair;
import vdsMain.message.GetBaseTxMessage;
import vdsMain.model.BlockChainModel;
import vdsMain.transaction.Transaction;
import vdsMain.wallet.Wallet;

import java.util.*;

public class CachedBlockList {

    //f12947a
    //910 f13082a
    public List<CachedBlockInfo> cachedBlockInfoList;

    //f12948b
    //910 f13083b
    public UInt256 lastBlockHash;

    //f13084c
    public Map<CTxDestination, Map<UInt256, List<UInt256>>> desToBlockhash2TxidsMap;

    //f12949c
    //910 f13085d
    private LinkedHashMap<UInt256, CachedBlockInfo> blockHashToCachedBlockInfoLinkedMap;

    //f12950d
    //910 f13086e
    private HashMap<UInt256, CachedBlockInfo> blockHashToCachedBlockInfoMap;

    //f12951e
    //910 f13087f
    private CachedBlockCreator cachedBlockCreator;

    public CachedBlockList(CachedBlockCreator... cachedBlockCreators) {
        this.desToBlockhash2TxidsMap =new HashMap<>();
        this.cachedBlockInfoList = new ArrayList();
        this.blockHashToCachedBlockInfoLinkedMap = new LinkedHashMap<>();
        this.blockHashToCachedBlockInfoMap = new HashMap<>();
        this.lastBlockHash = new UInt256();
        if (cachedBlockCreators.length < 1 || cachedBlockCreators[0] == null) {
            this.cachedBlockCreator = new CachedBlockCreator();
        } else {
            this.cachedBlockCreator = cachedBlockCreators[0];
        }
    }

    private CachedBlockList(CachedBlockList cachedBlockList) {
        this.desToBlockhash2TxidsMap =new HashMap<>();
        this.cachedBlockCreator = cachedBlockList.cachedBlockCreator;
        this.cachedBlockInfoList = new ArrayList(cachedBlockList.cachedBlockInfoList);
        this.blockHashToCachedBlockInfoLinkedMap = new LinkedHashMap<>(cachedBlockList.blockHashToCachedBlockInfoLinkedMap);
        this.blockHashToCachedBlockInfoMap = new HashMap<>(cachedBlockList.blockHashToCachedBlockInfoMap);
        this.lastBlockHash = new UInt256((BaseBlob) cachedBlockList.lastBlockHash);
    }

    //910 mo44041a
    public UInt256 getFirstCachedBlockInfoHash() {
        return ((CachedBlockInfo) this.cachedBlockInfoList.get(0)).blockHeader.getBlockHash();
    }

    //910 mo44043a
    public CachedBlockInfo getCachedBlockInfoByBlockHash(UInt256 uInt256) {
        return (CachedBlockInfo) this.blockHashToCachedBlockInfoLinkedMap.get(uInt256);
    }

    //910 mo44051b
    public UInt256 getLastCachedBlockInfoHash() {
        List<CachedBlockInfo> list = this.cachedBlockInfoList;
        return ((CachedBlockInfo) list.get(list.size() - 1)).blockHeader.getBlockHash();
    }

    //mo44044a
    public synchronized void clearAndInitAddressTxsMapByDesList(List<CTxDestination> desList) {
        this.desToBlockhash2TxidsMap.clear();
        for (CTxDestination des : desList) {
            this.desToBlockhash2TxidsMap.put(des, null);
        }
    }

    //910 mo44054c
    public synchronized CTxDestination getFirstNullCTxDestination() {
        for (Map.Entry entry : this.desToBlockhash2TxidsMap.entrySet()) {
            if (entry.getValue() == null) {
                return (CTxDestination) entry.getKey();
            }
        }
        return null;
    }

    //mo43979a
    public synchronized int getSumTransactionNumber() {
        int i;
        i = 0;
        for (CachedBlockInfo cachedBlockInfo : this.blockHashToCachedBlockInfoMap.values()) {
            i += cachedBlockInfo.sumTransactionNumber;
        }
        return i;
    }

    //mo43987b
    //910 mo44056e
    public synchronized int getSumErrorTxNumber() {
        int i;
        i = 0;
        for (CachedBlockInfo cachedBlockInfo : this.blockHashToCachedBlockInfoMap.values()) {
            i += cachedBlockInfo.sumErrorTxNumber;
        }
        return i;
    }

    //mo44059h
    public int getCachedBlockInfoMapSize() {
        return this.blockHashToCachedBlockInfoLinkedMap.size();
    }

    //mo44061j
    public synchronized CachedBlockList getHasTransactionCachedBlockList() {
        CachedBlockList cachedBlockList;
        cachedBlockList = new CachedBlockList(new CachedBlockCreator[0]);
        ArrayList<CachedBlockInfo> hasTransactionCachedBlockInfoList = new ArrayList();
        LinkedHashMap<UInt256, CachedBlockInfo> linkedHashMap = new LinkedHashMap<>();
        HashMap<UInt256, CachedBlockInfo> hashMap = new HashMap<>();
        UInt256 tempBlockHash = new UInt256();
        for (CachedBlockInfo cachedBlockInfo : this.cachedBlockInfoList) {
            if (!cachedBlockInfo.checkSumTransactionCountAndLinkedMap()) {
                break;
            }
            tempBlockHash = cachedBlockInfo.blockHeader.getBlockHash();
            hasTransactionCachedBlockInfoList.add(cachedBlockInfo);
            linkedHashMap.put(tempBlockHash, cachedBlockInfo);
            if (this.blockHashToCachedBlockInfoMap.containsKey(tempBlockHash)) {
                hashMap.put(tempBlockHash, cachedBlockInfo);
            }
            this.blockHashToCachedBlockInfoLinkedMap.remove(tempBlockHash);
            this.blockHashToCachedBlockInfoMap.remove(tempBlockHash);
        }
        this.cachedBlockInfoList.removeAll(hasTransactionCachedBlockInfoList);
        cachedBlockList.cachedBlockInfoList = hasTransactionCachedBlockInfoList;
        cachedBlockList.blockHashToCachedBlockInfoLinkedMap = linkedHashMap;
        cachedBlockList.blockHashToCachedBlockInfoMap = hashMap;
        cachedBlockList.lastBlockHash.set((BaseBlob) tempBlockHash);
        return cachedBlockList;
    }

    //910 mo44057f
    public synchronized CachedBlockList mo43989c() {
        return new CachedBlockList(this);
    }

    //0910 mo44048a
    public synchronized boolean mo43984a(Wallet wallet, List<BlockHeader> unSynchedBlockHeaderList) {
        BlockChainModel blockChainModel = wallet.getSelfBlockChainModel();
        for (BlockHeader blockHeader : unSynchedBlockHeaderList) {
            if (!mo43986a(blockChainModel, blockHeader, false)) {
                return false;
            }
        }
        return true;
    }

    //mo43981a
    public synchronized void setLastBlockHashAndAddToCachedBlockMap(List<BlockHeader> list) {
        clear();
        if (!list.isEmpty()) {
            this.lastBlockHash.set((BaseBlob) ((BlockHeader) list.get(list.size() - 1)).getBlockHash());
            for (BlockHeader blockHeader : list) {
                CachedBlockInfo cachedBlockInfo = this.cachedBlockCreator.getNewCachedBlockInfo(blockHeader, false);
                this.blockHashToCachedBlockInfoLinkedMap.put(blockHeader.getBlockHash(), cachedBlockInfo);
                this.cachedBlockInfoList.add(cachedBlockInfo);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:67:0x015b, code lost:
        return true;
     */
    /* renamed from: a */
    public synchronized boolean mo43986a(BlockChainModel blockChainModel, BlockHeader blockHeader, boolean... zArr) {
        boolean z;
        int i;
        CachedBlockInfo cachedBlockInfo = (CachedBlockInfo) this.blockHashToCachedBlockInfoLinkedMap.get(blockHeader.getBlockHash());
        boolean z2 = false;
        if (cachedBlockInfo != null) {
            int blockNo = cachedBlockInfo.blockHeader.getBlockNo();
            UInt256 preBlockHash = new UInt256((BaseBlob) cachedBlockInfo.blockHeader.getPreBlockHash());
            cachedBlockInfo.blockHeader.initFromBlockHeader(blockHeader);
            if (blockHeader.getPreBlockHash().isNull()) {
                cachedBlockInfo.blockHeader.setPreBlockHash(preBlockHash);
            }
            if (cachedBlockInfo.blockHeader.getBlockNo() == -1 && blockNo != -1) {
                cachedBlockInfo.blockHeader.setBlockNo(blockNo);
            }
            CachedBlockInfo preCachedBlockInfo = (CachedBlockInfo) this.blockHashToCachedBlockInfoLinkedMap.get(preBlockHash);
            if (preCachedBlockInfo != null) {
                cachedBlockInfo.blockHeader.setPreBlockHash(preBlockHash);
                if (preCachedBlockInfo.blockHeader.getBlockNo() != -1) {
                    cachedBlockInfo.blockHeader.setBlockNo(preCachedBlockInfo.blockHeader.getBlockNo() + 1);
                }
            } else if (blockHeader.isFirstBlock()) {
                blockHeader.setBlockNo(0);
            } else {
                BlockHeader b = blockChainModel.getBlockHeaderFromCachedAndDbByHash(blockHeader.getPreBlockHash());
                if (b == null) {
                    return false;
                }
                blockHeader.setBlockNo(b.getBlockNo() + 1);
            }
            i = blockNo;
            z = false;
        } else {
            i = blockHeader.getBlockNo();
            CachedBlockInfo preCachedBlockInfo = (CachedBlockInfo) this.blockHashToCachedBlockInfoLinkedMap.get(blockHeader.getPreBlockHash());
            if (preCachedBlockInfo == null) {
                if (!blockHeader.isFirstBlock()) {
                    BlockHeader preBlockHeader = blockChainModel.getBlockHeaderFromCachedAndDbByHash(blockHeader.getPreBlockHash());
                    if (preBlockHeader == null) {
                        return false;
                    }
                    blockHeader.setBlockNo(preBlockHeader.getBlockNo() + 1);
                    cachedBlockInfo = this.cachedBlockCreator.getNewCachedBlockInfo(blockHeader, zArr);
                    this.cachedBlockInfoList.add(cachedBlockInfo);
                    this.blockHashToCachedBlockInfoLinkedMap.put(blockHeader.getBlockHash(), cachedBlockInfo);
                } else if (!this.blockHashToCachedBlockInfoLinkedMap.isEmpty()) {
                    return false;
                } else {
                    blockHeader.setBlockNo(0);
                }
                z = true;
            } else if (!this.cachedBlockInfoList.isEmpty() && preCachedBlockInfo != this.cachedBlockInfoList.get(this.cachedBlockInfoList.size() - 1)) {
                return false;
            } else {
                cachedBlockInfo = this.cachedBlockCreator.getNewCachedBlockInfo(blockHeader, zArr);
                this.cachedBlockInfoList.add(cachedBlockInfo);
                this.blockHashToCachedBlockInfoLinkedMap.put(blockHeader.getBlockHash(), cachedBlockInfo);
                this.lastBlockHash.set((BaseBlob) blockHeader.getBlockHash());
                if (preCachedBlockInfo.blockHeader.getBlockNo() != -1) {
                    cachedBlockInfo.blockHeader.setBlockNo(preCachedBlockInfo.blockHeader.getBlockNo() + 1);
                }
                z = true;
            }
        }
        if (i == -1 && i != cachedBlockInfo.blockHeader.getBlockNo() && !z) {
            for (CachedBlockInfo imVar4 : this.cachedBlockInfoList) {
                if (imVar4 == cachedBlockInfo) {
                    i = imVar4.blockHeader.getBlockNo();
                    z2 = true;
                } else if (z2) {
                    i++;
                    imVar4.blockHeader.setBlockNo(i);
                }
            }
        }
        return true;
    }

    //mo43990d
    public Set<UInt256> getBlockHashSet() {
        return this.blockHashToCachedBlockInfoLinkedMap.keySet();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0043, code lost:
        return true;
     */
    /* renamed from: a */
    public synchronized boolean mo43985a(Wallet wallet, BlockHeader blockHeader, List<UInt256> list) {
        if (!mo43986a(wallet.getSelfBlockChainModel(), blockHeader, true)) {
            return false;
        }
        CachedBlockInfo cachedBlockInfo = (CachedBlockInfo) this.blockHashToCachedBlockInfoLinkedMap.get(blockHeader.getBlockHash());
        cachedBlockInfo.mo43974a(list, true);
        if (list != null) {
            if (!list.isEmpty()) {
                this.blockHashToCachedBlockInfoMap.put(blockHeader.getBlockHash(), cachedBlockInfo);
                return true;
            }
        }
        this.blockHashToCachedBlockInfoMap.remove(blockHeader.getBlockHash());
        return false;
    }

    //910 mo44046a
    public synchronized boolean mo43982a(Transaction dhVar) {
        for (Map.Entry entry : this.blockHashToCachedBlockInfoMap.entrySet()) {
            CachedBlockInfo imVar = (CachedBlockInfo) entry.getValue();
            if (imVar.mo43976a(dhVar.getTxId())) {
                dhVar.setBlockHash((UInt256) entry.getKey());
                imVar.txidToTransactionLinkedMap.put(dhVar.getTxId(), dhVar);
                return true;
            }
        }
        return false;
    }

    //mo43980a
    public BlockHeader getBlockHeaderFromLinkedMap(UInt256 uInt256) {
        CachedBlockInfo cachedBlockInfo = (CachedBlockInfo) this.blockHashToCachedBlockInfoLinkedMap.get(uInt256);
        if (cachedBlockInfo == null) {
            return null;
        }
        return cachedBlockInfo.blockHeader;
    }

    //mo43988b
    public boolean isLinkedMapHasKey(UInt256 uInt256) {
        return this.blockHashToCachedBlockInfoLinkedMap.containsKey(uInt256);
    }

    //mo43991e
    //910 mo44060i
    public boolean isBlockHashToCachedBlockMapEmpty() {
        return this.blockHashToCachedBlockInfoLinkedMap.isEmpty();
    }

    //mo43983a
    ///910 mo44047a
    public synchronized boolean getTroubleAndUNSynchedBlockStartToEndHeight(Pair<Integer, Integer> pair, List<CachedBlockInfo>... listArr) {
        boolean noTrouble;
        List<CachedBlockInfo> list = listArr.length > 0 ? listArr[0] : null;
        if (list != null) {
            list.clear();
        }
        int startHeight = -1;
        int endHeight = -1;
        noTrouble = true;
        for (CachedBlockInfo cachedBlockInfo : this.cachedBlockInfoList) {
            if (!cachedBlockInfo.checkSumTransactionCountAndLinkedMap()) {
                if (list != null) {
                    list.add(cachedBlockInfo);
                }
                if (startHeight == -1) {
                    startHeight = cachedBlockInfo.blockHeader.getBlockNo();
                    endHeight = startHeight;
                    noTrouble = false;
                } else if (cachedBlockInfo.blockHeader.getBlockNo() > endHeight) {
                    endHeight = cachedBlockInfo.blockHeader.getBlockNo();
                    noTrouble = false;
                } else {
                    noTrouble = false;
                }
            }
        }
        if (pair != null) {
            pair.key = Integer.valueOf(startHeight);
            pair.value = Integer.valueOf(endHeight);
        }
        return noTrouble;
    }

    //mo43992f
    //910 mo44062k
    public synchronized void clear() {
        this.cachedBlockInfoList.clear();
        this.lastBlockHash.setNull();
        this.blockHashToCachedBlockInfoLinkedMap.clear();
        this.blockHashToCachedBlockInfoMap.clear();
    }

    //mo43993g
    //910 mo44063l
    public BlockHeader getFirstBlockHeader() {
        if (this.cachedBlockInfoList.isEmpty()) {
            return null;
        }
        return ((CachedBlockInfo) this.cachedBlockInfoList.get(0)).blockHeader;
    }

    public synchronized void testFull() {
        for (Map.Entry entry : this.desToBlockhash2TxidsMap.entrySet()) {
            entry.setValue(new HashMap<>());
        }
    }

    public synchronized void mo44045a(CTxDestination des, Map<UInt256, List<UInt256>> map) {
        this.desToBlockhash2TxidsMap.put(des, map);
    }

    //910 mo44064m
    //mo43994h
    public BlockHeader getLastIndexBlockHeader() {
        List<CachedBlockInfo> list = this.cachedBlockInfoList;
        if (list == null || list.isEmpty()) {
            return null;
        }
        List<CachedBlockInfo> list2 = this.cachedBlockInfoList;
        return ((CachedBlockInfo) list2.get(list2.size() - 1)).blockHeader;
    }


    //mo44042a
    public synchronized GetBaseTxMessage getNewBaseTxMessage(Wallet wallet) {
        for (CachedBlockInfo cachedBlockInfo : this.cachedBlockInfoList) {
            if (!cachedBlockInfo.checkSumTransactionCountAndLinkedMap()) {
                UInt256 blockHash = cachedBlockInfo.blockHeader.getBlockHash();
                HashMap hashMap = null;
                for (Map map : this.desToBlockhash2TxidsMap.values()) {
                    List<UInt256> list = (List) map.get(blockHash);
                    if (list != null && !list.isEmpty()) {
                        if (hashMap == null) {
                            hashMap = new HashMap();
                        }
                        for (UInt256 put : list) {
                            hashMap.put(put, null);
                        }
                    }
                }
                if (hashMap != null) {
                    if (!hashMap.isEmpty()) {
                        GetBaseTxMessage getBaseTxMessage = new GetBaseTxMessage(wallet);
                        getBaseTxMessage.setBlockHash(blockHash);
                        getBaseTxMessage.setTxIdMap((Map<UInt256, Object>) hashMap);
                        return getBaseTxMessage;
                    }
                }
                cachedBlockInfo.setSumNumberAndAddtoLinkedMap(null, true);
            }
        }
        return null;
    }
}
