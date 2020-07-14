package com.vtoken.application.util;

import com.vtoken.application.ApplicationLoader;
import vdsMain.CTxDestination;
import vdsMain.model.HDAccount;
import vdsMain.transaction.Transaction;
import vdsMain.transaction.TxIn;
import vdsMain.model.Address;
import vdsMain.transaction.TxOut;

import java.util.*;

//bbx
public class WalletUtil {

    public static void m7067a(List<Transaction> list) {
        if (list != null && !list.isEmpty()) {
            Collections.sort(list, new Comparator<Transaction>() {
                /* renamed from: a */
                public int compare(Transaction dhVar, Transaction dhVar2) {
                    long I = dhVar.getTimeStamp();
                    long I2 = dhVar2.getTimeStamp();
                    if (I == 0) {
                        return -1;
                    }
                    if (I2 == 0) {
                        return 1;
                    }
                    int i = (I > I2 ? 1 : (I == I2 ? 0 : -1));
                    if (i == 0) {
                        return dhVar.getTxidHashString().compareTo(dhVar2.getTxidHashString());
                    }
                    if (i > 0) {
                        return -1;
                    }
                    if (i < 0) {
                        return 1;
                    }
                    return 0;
                }
            });
        }
    }

    public static long m7076g(Transaction transaction) {
        List<TxIn> txInList = transaction.getSelfTxInList();
        HashSet<CTxDestination> hashSet = new HashSet<>();
        List<Address> addressVector = ((HDAccount) ApplicationLoader.getVcashCore().getHDAccountList().get(0)).getAddressVector();
        ArrayList arrayList = new ArrayList();
        for (Address address : addressVector) {
            arrayList.add(address.getCTxDestination());
        }
        for (TxIn txIn : txInList) {
            CTxDestination txInCTxDestination = txIn.getCTxDestination();
            if (arrayList.contains(txInCTxDestination)) {
                hashSet.add(txInCTxDestination);
            }
        }
        for (TxOut txOut : transaction.getSelfTxOutList()) {
            CTxDestination txOutScriptCTxDestination = txOut.getScriptCTxDestination();
            if (arrayList.contains(txOutScriptCTxDestination)) {
                hashSet.add(txOutScriptCTxDestination);
            }
        }
        if (hashSet.isEmpty()) {
            return transaction.mo43064n();
        }
        long j = 0;
        for (CTxDestination cTxDestination : hashSet) {
            j += transaction.getVoutSumSubVinSum(cTxDestination);
        }
        return j;
    }
}
