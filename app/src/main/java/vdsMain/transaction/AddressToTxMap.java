package vdsMain.transaction;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.utils.AddressUtils;
import vdsMain.CTxDestination;
import vdsMain.wallet.Wallet;

import java.util.*;

public class AddressToTxMap {

    //f12881a
    private Wallet wallet;

    //f12882b
    private HashMap<CTxDestination, LinkedHashMap<UInt256, Boolean>> cTxDesToTxid2BoolMap = new HashMap<>();

    public AddressToTxMap(@NonNull Wallet izVar) {
        this.wallet = izVar;
    }

    //mo43952a
    public synchronized List<Transaction> getTransactionListByConfirmType(CTxDestination cTxDestination, TransactionConfirmType transactionConfirmType) {
        if (cTxDestination == null) {
            return new Vector(1);
        }
        LinkedHashMap<UInt256,Boolean> linkedHashMap = (LinkedHashMap) this.cTxDesToTxid2BoolMap.get(cTxDestination);
        if (linkedHashMap != null) {
            if (!linkedHashMap.isEmpty()) {
                ArrayList arrayList = new ArrayList();
                for (Map.Entry key : linkedHashMap.entrySet()) {
                    Transaction transaction = this.wallet.getTransactionFromAllMap((UInt256) key.getKey());
                    if (transaction != null) {
                        if (transactionConfirmType == TransactionConfirmType.ALL) {
                            arrayList.add(transaction);
                        } else if (transactionConfirmType == TransactionConfirmType.CONFIRMED) {
                            if (transaction.isConfirmed()) {
                                arrayList.add(transaction);
                            }
                        } else if (transactionConfirmType == TransactionConfirmType.UNCONFIRMED && !transaction.isConfirmed()) {
                            arrayList.add(transaction);
                        }
                    }
                }
                return arrayList;
            }
        }
        return new Vector(1);
    }

    //mo43954a
    public synchronized void removeFromCTxDesToTxid2BoolByTxid(UInt256 txid) {
        ArrayList<CTxDestination> removeDesList = new ArrayList<>();
        for (Map.Entry<CTxDestination, LinkedHashMap<UInt256, Boolean>> entry : this.cTxDesToTxid2BoolMap.entrySet()) {
            LinkedHashMap<UInt256, Boolean> linkedHashMap =entry.getValue();
            if (linkedHashMap.containsKey(txid) && linkedHashMap.remove(txid) && linkedHashMap.isEmpty()) {
                removeDesList.add(entry.getKey());
            }
        }
        for(CTxDestination des : removeDesList){
            this.cTxDesToTxid2BoolMap.remove(des);
        }
    }

    //mo43955a
    public synchronized void checkAndCTxDestToTxid2True(CTxDestination cTxDestination, UInt256 uInt256) {
        LinkedHashMap linkedHashMap = (LinkedHashMap) this.cTxDesToTxid2BoolMap.get(cTxDestination);
        if (linkedHashMap == null) {
            LinkedHashMap linkedHashMap2 = new LinkedHashMap();
            linkedHashMap2.put(uInt256, Boolean.valueOf(true));
            this.cTxDesToTxid2BoolMap.put(cTxDestination, linkedHashMap2);
        } else if (!linkedHashMap.containsKey(uInt256)) {
            linkedHashMap.put(uInt256, Boolean.valueOf(true));
        }
    }

    /* renamed from: a */
    public List<UInt256> mo43950a(String str) {
        return mo43951a(AddressUtils.m938a(str, this.wallet.getChainParams()));
    }

    /* renamed from: a */
    public synchronized List<UInt256> mo43951a(CTxDestination oVar) {
        if (oVar == null) {
            return new Vector(1);
        }
        LinkedHashMap<UInt256, Boolean> linkedHashMap = (LinkedHashMap) this.cTxDesToTxid2BoolMap.remove(oVar);
        if (linkedHashMap != null) {
            if (!linkedHashMap.isEmpty()) {
                Vector vector = new Vector(linkedHashMap.size());
                for (Map.Entry key : linkedHashMap.entrySet()) {
                    vector.add(key.getKey());
                }
                return vector;
            }
        }
        return new Vector(1);
    }

    //mo43953a
    public synchronized void clear() {
        this.cTxDesToTxid2BoolMap.clear();
    }
}
