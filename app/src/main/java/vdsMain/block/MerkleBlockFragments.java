package vdsMain.block;

import bitcoin.UInt256;
import vdsMain.Pair;
import vdsMain.wallet.Wallet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MerkleBlockFragments {

    //f13227a
    private HashMap<UInt256, Pair<BlockHeader, List<UInt256>>> hashToBlockHeaderHashPairMap = new HashMap<>();

    //f13228b
    private HashMap<UInt256, UInt256> blockHashToHashMap = new HashMap<>();

    /* renamed from: c */
    private long f13229c = 0;

    /* renamed from: a */
    public synchronized void mo44544a(BlockHeader blockHeader, List<UInt256> list) {
        this.f13229c = System.currentTimeMillis();
        UInt256 e = blockHeader.getBlockHash();
        this.hashToBlockHeaderHashPairMap.put(e, new Pair(blockHeader, list));
        if (list != null && !list.isEmpty()) {
            for (UInt256 put : list) {
                this.blockHashToHashMap.put(put, e);
            }
        }
    }

    /* renamed from: a */
    public boolean mo44546a(UInt256 uInt256) {
        return this.hashToBlockHeaderHashPairMap.containsKey(uInt256);
    }

    //mo44547a
    public synchronized boolean isAllPreBlockInCache(Wallet wallet, CachedBlockList cachedBlockList) {
        for (Map.Entry<UInt256, Pair<BlockHeader, List<UInt256>>> value : this.hashToBlockHeaderHashPairMap.entrySet()) {
            Pair pair = value.getValue();
            if (pair == null) {
                return false;
            }
            if (pair.key == null) {
                return false;
            }
            BlockHeader blockHeader = (BlockHeader) pair.key;
            UInt256 preBlockHash = blockHeader.getPreBlockHash();
            Pair preBlockPair = (Pair) this.hashToBlockHeaderHashPairMap.get(preBlockHash);
            if (preBlockPair == null) {
                if (!blockHeader.isFirstBlock()) {
                    if (cachedBlockList == null && !cachedBlockList.isLinkedMapHasKey(preBlockHash)) {
                        if (!wallet.isBlockHashInCacheOrDb(preBlockHash, true)) {
                            return false;
                        }
                    }
                }
            } else if (preBlockPair.key == null) {
                return false;
            }
        }
        return true;
    }

    //循坏pairMap的迭代器，把当前的加入链表，再查找前面的连续区块插入链表
    //mo44548b
    public synchronized List<Pair<BlockHeader, List<UInt256>>> getBlockHeaderToHashListPairList(Wallet wallet, CachedBlockList inVar) {
        LinkedList linkedList;
        linkedList = new LinkedList();
        while (!this.hashToBlockHeaderHashPairMap.isEmpty()) {
            Pair removePair = (Pair) this.hashToBlockHeaderHashPairMap.remove(this.hashToBlockHeaderHashPairMap.keySet().iterator().next());
            linkedList.add(removePair);
            int indexOf = linkedList.indexOf(removePair);
            Pair preBlockPair = (Pair) this.hashToBlockHeaderHashPairMap.remove(((BlockHeader) removePair.key).getPreBlockHash());
            while (preBlockPair != null) {
                linkedList.add(indexOf, preBlockPair);
                preBlockPair = (Pair) this.hashToBlockHeaderHashPairMap.remove(((BlockHeader) preBlockPair.key).getPreBlockHash());
            }
        }
        this.f13229c = 0;
        this.blockHashToHashMap.clear();
        return linkedList;
    }

    //mo44545a
    public boolean isHashToBlockHeaderHashPairMapEmpty() {
        return this.hashToBlockHeaderHashPairMap.isEmpty();
    }

    //mo44549b
    //910 mo44632b
    public synchronized void clear() {
        this.f13229c = 0;
        this.hashToBlockHeaderHashPairMap.clear();
        this.blockHashToHashMap.clear();
    }

    //910 mo44633b
    public boolean mo44550b(UInt256 uInt256) {
        return this.blockHashToHashMap.containsKey(uInt256);
    }
}
