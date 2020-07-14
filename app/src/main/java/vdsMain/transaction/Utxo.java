package vdsMain.transaction;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import vdsMain.AddressType;
import vdsMain.CTxDestination;
import vdsMain.wallet.Wallet;

public class Utxo {

    //f13364a
    protected TxOut txOut;

    //f13365b
    protected SaplingUtxoValue saplingUtxoValue;

    //f13366c
    protected Wallet wallet;

    //f13367d
    protected boolean isLock;

    public Utxo(@NonNull TxOut txOut, boolean... isLocks) {
        this.txOut = txOut;
        this.wallet = txOut.getSelfWallet();
        boolean isLock = false;
        if (isLocks.length > 0) {
            isLock = isLocks[0];
        }
        this.isLock = isLock;
    }

    public Utxo(@NonNull Wallet wallet, @NonNull SaplingUtxoValue bpn, boolean... isLocks) {
        this.wallet = wallet;
        this.saplingUtxoValue = bpn;
        boolean isLock = false;
        if (isLocks.length > 0) {
            isLock = isLocks[0];
        }
        this.isLock = isLock;
    }

    /* renamed from: a */
    public AddressType mo44691a() {
        if (this.txOut != null) {
            return AddressType.GENERAL;
        }
        return AddressType.ANONYMOUS;
    }

    //mo44693b
    public CTxDestination getSaplingUtxoDes() {
        TxOut txOut = this.txOut;
        if (txOut != null) {
            return txOut.getScriptCTxDestination();
        }
        return this.saplingUtxoValue.cTxDestination;
    }

    /* renamed from: a */
    public void mo44692a(String str) {
        TxOut dnVar = this.txOut;
        if (dnVar != null) {
            dnVar.mo43279a(str);
        } else {
            this.saplingUtxoValue.saplingOutpoint.txid.setHex(str);
        }
    }

    //mo44694c
    public UInt256 getTxOutTxid() {
        TxOut txOut = this.txOut;
        if (txOut != null) {
            return txOut.getMTxid();
        }
        SaplingUtxoValue saplingUtxoValue = this.saplingUtxoValue;
        if (saplingUtxoValue != null) {
            return saplingUtxoValue.saplingOutpoint.txid;
        }
        return null;
    }

    /* renamed from: d */
    public int mo44695d() {
        TxOut txOut = this.txOut;
        if (txOut != null) {
            return txOut.getIndex();
        }
        return this.saplingUtxoValue.saplingOutpoint.index;
    }

    //mo44696e
    public COutPoint getCOutPoint() {
        TxOut txOut = this.txOut;
        if (txOut != null) {
            return txOut.getCOutPoint();
        }
        return this.saplingUtxoValue.saplingOutpoint;
    }

    //mo44697f
    public long getValue() {
        TxOut txOut = this.txOut;
        if (txOut != null) {
            return txOut.getSatoshi();
        }
        return this.saplingUtxoValue.value;
    }

    //mo44698g
    public boolean getIsLock() {
        return this.isLock;
    }

    /* renamed from: h */
    public TxOut mo44699h() {
        return this.txOut;
    }
}
