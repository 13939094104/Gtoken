package vdsMain.block;

import android.os.Build;
import androidx.annotation.NonNull;
import bitcoin.UInt256;
import vdsMain.Log;
import vdsMain.transaction.Transaction;
import vdsMain.wallet.Wallet;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class CachedBlockInfo {

    //f12943a
    //910 f13078a
    public BlockHeader blockHeader;

    //f12944b
    public LinkedHashMap<UInt256, Transaction> txidToTransactionLinkedMap;

    //f12945c
    public int sumTransactionNumber = -1;

    //f12946d
    public int sumErrorTxNumber = 0;

    public CachedBlockInfo(@NonNull BlockHeader blockHeader, boolean... zArr) {
        this.blockHeader = blockHeader;
        if (zArr.length < 1 || zArr[0]) {
            this.sumTransactionNumber = 0;
        } else {
            this.sumTransactionNumber = -1;
        }
    }

    //mo43975a
    //910 mo44037a
    public synchronized boolean checkSumTransactionCountAndLinkedMap() {
        if (this.sumTransactionNumber < 0) {
            return false;
        }
        if (this.txidToTransactionLinkedMap != null) {
            if (!this.txidToTransactionLinkedMap.isEmpty()) {
                if (this.sumTransactionNumber != this.txidToTransactionLinkedMap.size()) {
                    return false;
                }
                for (Map.Entry value : this.txidToTransactionLinkedMap.entrySet()) {
                    if (value.getValue() == null) {
                        return false;
                    }
                }
                return true;
            }
        }
        if (this.sumTransactionNumber != 0) {
            return false;
        }
        return true;
    }

    /* renamed from: a */
    public boolean mo43976a(UInt256 uInt256) {
        if (uInt256.isNull()) {
            return false;
        }
        LinkedHashMap<UInt256, Transaction> linkedHashMap = this.txidToTransactionLinkedMap;
        if (linkedHashMap == null || linkedHashMap.isEmpty()) {
            return false;
        }
        return this.txidToTransactionLinkedMap.containsKey(uInt256);
    }

    /* renamed from: a */
    public synchronized void mo43974a(List<UInt256> list, boolean... zArr) {
        if (list != null) {
            if (!list.isEmpty()) {
                this.sumTransactionNumber = list.size();
                this.txidToTransactionLinkedMap = new LinkedHashMap<>(1);
                for (UInt256 put : list) {
                    this.txidToTransactionLinkedMap.put(put, null);
                }
                return;
            }
        }
        if (zArr.length >= 1) {
            if (!zArr[0]) {
                this.sumTransactionNumber = -1;
                this.txidToTransactionLinkedMap = null;
            }
        }
        this.sumTransactionNumber = 0;
        this.txidToTransactionLinkedMap = null;
    }

    //910 mo44035a
    public synchronized void mo43973a(List<UInt256> list) {
        list.clear();
        if (this.txidToTransactionLinkedMap != null && !this.txidToTransactionLinkedMap.isEmpty()) {
            list.addAll(this.txidToTransactionLinkedMap.keySet());
        }
    }

    //mo43978b
    //910 mo44040b
    public synchronized void setSumNumberAndAddtoLinkedMap(List<Transaction> list, boolean... zArr) {
        if (list != null) {
            if (!list.isEmpty()) {
                if (this.sumTransactionNumber == -1) {
                    this.sumTransactionNumber = list.size();
                }
                this.txidToTransactionLinkedMap = new LinkedHashMap<>(list.size());
                for (Transaction transaction : list) {
                    //Log.info("关联事务id",transaction.getTxId().hashString());
                    this.txidToTransactionLinkedMap.put(transaction.getTxId(), transaction);
                }
                return;
            }
        }
        if (zArr.length >= 1) {
            if (!zArr[0]) {
                this.sumTransactionNumber = -1;
                this.txidToTransactionLinkedMap = null;
            }
        }
        this.sumTransactionNumber = 0;
        this.txidToTransactionLinkedMap = null;
    }

    //mo43971a
    public synchronized BlockInfo getBlockInfoAndSetTransactionList(@NonNull Wallet wallet) {
        BlockInfo blockInfo;
        blockInfo = wallet.getSelfWalletHelper().getBlockInfoFromBlockHeader(this.blockHeader);
        if (this.txidToTransactionLinkedMap != null && !this.txidToTransactionLinkedMap.isEmpty()) {
            blockInfo.setTransactionList(new Vector(this.txidToTransactionLinkedMap.values()), false);
        }
        return blockInfo;
    }

    //mo44034a
    public synchronized void mo43972a(UInt256 uInt256, Transaction transaction) {
        if (Build.VERSION.SDK_INT >= 24) {
            this.txidToTransactionLinkedMap.replace(uInt256, transaction);
        } else {
            this.txidToTransactionLinkedMap.put(uInt256, transaction);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0021, code lost:
        return;
     */
    //910 mo44039b
    public synchronized void mo43977b(UInt256 uInt256) {
        if (this.txidToTransactionLinkedMap != null) {
            if (this.txidToTransactionLinkedMap.containsKey(uInt256)) {
                this.sumTransactionNumber--;
                this.sumErrorTxNumber++;
                this.txidToTransactionLinkedMap.remove(uInt256);
            }
        }
    }
}
