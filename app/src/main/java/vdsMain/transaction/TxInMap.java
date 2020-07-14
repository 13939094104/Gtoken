package vdsMain.transaction;

import bitcoin.UInt256;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class TxInMap {

    //f12440a
    private Multimap<COutPoint, TxIn> prevTxOut2TxInMultiMap = HashMultimap.create();

    //mo43314a
    public synchronized void checkAndAddPreTxOut2TxInSynchronized(TxIn dlVar) {
        checkAndAddPreTxOut2TxIn(dlVar);
    }

    //m11022b
    private void checkAndAddPreTxOut2TxIn(TxIn txIn) {
        checkTxIn(txIn);
        COutPoint prevTxOut = txIn.getPrevTxOut();
        Collection collection = this.prevTxOut2TxInMultiMap.get(prevTxOut);
        if (collection == null || collection.isEmpty()) {
            this.prevTxOut2TxInMultiMap.put(prevTxOut, txIn);
        } else if (!collection.contains(txIn)) {
            this.prevTxOut2TxInMultiMap.put(prevTxOut, txIn);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0024, code lost:
        return;
     */
    /* renamed from: a */
    public synchronized void mo43315a(List<TxIn> list) {
        if (list != null) {
            if (!list.isEmpty()) {
                for (TxIn b : list) {
                    checkAndAddPreTxOut2TxIn(b);
                }
            }
        }
    }

    /* renamed from: a */
    public void mo43312a(TransactionInteface diVar) {
        mo43315a(diVar.getSelfTxInList());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0034, code lost:
        return;
     */
    /* renamed from: a */
    public synchronized void mo43313a(TransactionInteface diVar, Collection<UInt256> collection) {
        HashMap hashMap = new HashMap();
        List<TxOut> d = diVar.getSelfTxOutList();
        if (d != null) {
            if (!d.isEmpty()) {
                for (TxOut txOut : d) {
                    getTxInTxidAndAddToCollectionSynchronized(txOut.getCOutPoint(), collection, hashMap);
                }
            }
        }
    }

    //mo43311a
    public synchronized void getTxInTxidAndAddToCollectionSynchronized(COutPoint cOutPoint, Collection<UInt256> collection, HashMap<UInt256, UInt256>... hashMapArr) {
        getTxInTxidAndAddToCollection(cOutPoint, collection, hashMapArr);
    }

    //mo43316b
    public void getTxInTxidAndAddToCollection(COutPoint cOutPoint, Collection<UInt256> collection, HashMap<UInt256, UInt256>... hashMapArr) {
        Collection<TxIn> txIns = this.prevTxOut2TxInMultiMap.get(cOutPoint);
        if (txIns != null && !txIns.isEmpty()) {
            HashMap<UInt256, UInt256> hashMap = null;
            if (hashMapArr.length > 0 && hashMapArr[0] != null) {
                hashMap = hashMapArr[0];
            } else if (txIns.size() > 1) {
                hashMap = new HashMap<>();
            }
            for (TxIn txIn : txIns) {
                UInt256 txid = txIn.getMTxid();
                if (hashMap == null || !hashMap.containsKey(txid)) {
                    collection.add(txid);
                    if (hashMap != null) {
                        hashMap.put(txid, txid);
                    }
                }
            }
        }
    }

    //mo43317b
    //910 mo43380b
    public void checkAndRemoveTxInForTransaction(TransactionInteface transactionInteface) {
        checkAndRemoveTxInForAllList(transactionInteface.getSelfTxInList());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0024, code lost:
        return;
     */
    //mo43318b
    public synchronized void checkAndRemoveTxInForAllList(List<TxIn> list) {
        if (list != null) {
            if (!list.isEmpty()) {
                for (TxIn txIn : list) {
                    checkAndRemoveTxInFromMap(txIn);
                }
            }
        }
    }

    //m11023c
    //910 m11148c
    private void checkAndRemoveTxInFromMap(TxIn txIn) {
        checkTxIn(txIn);
        this.prevTxOut2TxInMultiMap.remove(txIn.getPrevTxOut(), txIn);
    }

    //m11024d
    private void checkTxIn(TxIn txIn) {
        UInt256 txid = txIn.getMTxid();
        if (txid == null || txid.isNull()) {
            throw new IllegalArgumentException("Vin must contains txid!");
        } else if (txIn.getPrevTxOut() == null) {
            throw new IllegalArgumentException("Vin must contains previors vout info.");
        }
    }

    /* renamed from: a */
    public synchronized List<TxIn> mo43309a(COutPoint ciVar) {
        Collection collection = this.prevTxOut2TxInMultiMap.get(ciVar);
        if (collection != null) {
            if (!collection.isEmpty()) {
                return new Vector(collection);
            }
        }
        return null;
    }

    //mo43310a
    public synchronized void clear() {
        this.prevTxOut2TxInMultiMap.clear();
    }

    //mo43319b
    public boolean hasKey(COutPoint cOutPoint) {
        return this.prevTxOut2TxInMultiMap.containsKey(cOutPoint);
    }
}