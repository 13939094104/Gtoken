package vdsMain.transaction;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import org.jetbrains.annotations.NotNull;
import vdsMain.WalletSeriablData;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.List;

public class SeriableOuts extends WalletSeriablData implements TransactionInteface {

    //f12992a
    List<TxOut> txOutList;

    /* renamed from: a */
    public void setFlag(int i) {
    }

    /* renamed from: a */
    public void setVersion(long j) {
    }

    /* renamed from: b */
    public TransactionInteface clone() {
        return null;
    }

    /* renamed from: b */
    public void checkAndSetLockTime(long j) {
    }

    /* renamed from: e */
    public List<TxIn> getSelfTxInList() {
        return null;
    }

    /* renamed from: f */
    public long getVersion() {
        return 0;
    }

    /* renamed from: g */
    public long getLockTime() {
        return 0;
    }

    /* renamed from: h */
    public int getFlag() {
        return 0;
    }

    /* renamed from: h_ */
    public UInt256 getTxId() {
        return null;
    }

    /* renamed from: i_ */
    public UInt256 updateTxidByContent() {
        return null;
    }

    public void onDecodeSerialData() {
    }

    public SeriableOuts(@NonNull Wallet wallet) {
        super(wallet);
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        TransactionSerializer.writeTxOutList((TransactionInteface) this, streamWriter);
    }

    /* renamed from: d */
    public List<TxOut> getSelfTxOutList() {
        return this.txOutList;
    }

    //mo44015a
    public void setTxOutList(List<TxOut> list) {
        this.txOutList = list;
    }

    @NotNull
    @Override
    public Wallet getWallet() {
        return null;
    }
}
