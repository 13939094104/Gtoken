package vdsMain.transaction;

import androidx.annotation.NonNull;
import bitcoin.CNoDestination;
import bitcoin.CPubKey;
import bitcoin.script.CScript;
import bitcoin.script.WitnessV0KeyHash;
import bitcoin.script.WitnessV0ScriptHash;
import com.vc.libcommon.exception.AddressFormatException;
import com.vc.libcommon.util.UInt;
import generic.io.StreamWriter;
import generic.utils.AddressUtils;
import org.spongycastle.asn1.cmc.BodyPartID;
import vdsMain.CTxDestination;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class TxIn extends TxBase {

    //f12436g
    protected COutPoint mPrevTxOut;

    //f12437h
    protected Script mScriptSig;

    //f12438i
    protected long mSequence;

    //f12439j
    protected CScriptWitness mScriptWitness;

    public TxIn(@NonNull Wallet wallet) {
        super(wallet);
        this.mSequence = UInt.f672b.mo18934c();
        this.mPrevTxOut = wallet.getSelfWalletHelper().getNewCOutPoint();
        reComputeIndexAndHash();
    }

    public TxIn(TxIn txIn) {
        super((TxBase) txIn);
        this.mSequence = UInt.f672b.mo18934c();
        this.mPrevTxOut = this.wallet.getSelfWalletHelper().getNewCOutPoint();
        this.mPrevTxOut.mo43130a(txIn.mPrevTxOut);
        this.mScriptSig = new Script(txIn.mScriptSig);
        this.mSequence = txIn.mSequence;
        CScriptWitness clVar = txIn.mScriptWitness;
        if (clVar == null) {
            this.mScriptWitness = null;
        } else {
            this.mScriptWitness = new CScriptWitness(clVar);
        }
        reComputeIndexAndHash();
    }

    public TxIn(TxOut txOut) {
        super(txOut.getSelfWallet());
        this.mSequence = UInt.f672b.mo18934c();
        this.mPrevTxOut = this.wallet.getSelfWalletHelper().getNewCOutPoint();
        this.mPrevTxOut.setTxidAndInexFromOther(txOut);
        if (txOut.getScript() != null) {
            this.mScriptSig = new Script(txOut.getScript());
        } else {
            this.mScriptSig = null;
        }
        this.mFlag=txOut.mFlag;
        reComputeIndexAndHash();
    }

    //mo43296j
    public COutPoint getPrevTxOut() {
        return this.mPrevTxOut;
    }

    //mo43297k
    public Script getScriptSig() {
        return this.mScriptSig;
    }

    /* renamed from: a */
    public void mo43292a(Script script) {
        Script crVar2 = this.mScriptSig;
        if (crVar2 != null) {
            crVar2.clean();
        }
        if (script != null && !script.isScriptChunkListEmpty()) {
            Script crVar3 = this.mScriptSig;
            if (crVar3 == null) {
                this.mScriptSig = new Script(script);
            } else {
                crVar3.mo43154a(script);
            }
        }
    }

    //mo43298l
    public boolean hasCscriptWitness() {
        CScriptWitness cScriptWitness = this.mScriptWitness;
        return cScriptWitness != null && !cScriptWitness.isBytesListEmpty();
    }

    //mo43299m
    public CScriptWitness getCScriptWitness() {
        return this.mScriptWitness;
    }

    //mo43291a
    public void setCScriptWitness(CScriptWitness cScriptWitness, boolean... zArr) {
        if (zArr.length > 0 && zArr[0]) {
            this.mScriptWitness = cScriptWitness;
        } else if (cScriptWitness == null) {
            this.mScriptWitness = null;
        } else {
            this.mScriptWitness = new CScriptWitness(cScriptWitness);
        }
    }

    //mo43300n
    public long getSequence() {
        return this.mSequence;
    }

    //mo43295d
    public void setSequence(long j) {
        this.mSequence = j;
    }

    //mo43293a
    public void setPreTxOut(TxOut txOut) {
        if (txOut != null) {
            this.mPrevTxOut.setTxidAndInexFromOther(txOut);
            this.mFlag=txOut.getFlag();
        } else {
            this.mPrevTxOut.clear();
        }
        reComputeIndexAndHash();
    }

    //mo43301o
    public TxOut getPrevTransactionTxOut() {
        Transaction transaction = this.wallet.getTransactionFromAllMap(this.mPrevTxOut.txid);
        if (transaction != null) {
            return transaction.getTxOut(this.mPrevTxOut.index);
        }
        return null;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(super.toString());
        StringBuilder sb = new StringBuilder();
        sb.append("\nmPrevTxOut: ");
        sb.append(this.mPrevTxOut.toString());
        stringBuffer.append(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("\nmScriptSig: ");
        sb2.append(this.mScriptSig);
        stringBuffer.append(sb2.toString());
        if (this.mScriptWitness != null) {
            stringBuffer.append("\nmScriptWitness: ");
            stringBuffer.append(this.mScriptWitness.toString());
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("\nmSequence: ");
        sb3.append(this.mSequence);
        stringBuffer.append(sb3.toString());
        return stringBuffer.toString();
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        this.mPrevTxOut.serialToStream(streamWriter);
        Script crVar = this.mScriptSig;
        if (crVar != null) {
            byte[] c = crVar.getScriptChunksBytes();
            if (c == null) {
                streamWriter.writeVariableInt(0);
            } else {
                streamWriter.writeVariableInt((long) c.length);
            }
            streamWriter.write(c);
        } else {
            streamWriter.writeVariableInt(0);
        }
        streamWriter.writeUInt32T(this.mSequence);
    }

    public void onDecodeSerialData() {
        this.mPrevTxOut.decodeSerialStream(getTempInput());
        this.mScriptSig = new Script();
        this.mScriptSig.decodeSerialStream(getTempInput());
        this.mSequence = readUInt32();
        reComputeIndexAndHash();
    }

    //mo43302p
    public boolean isCoinBaseVin() {
        return this.mPrevTxOut.txid.isNull() && (((long) this.mPrevTxOut.index) & BodyPartID.bodyIdMax) == BodyPartID.bodyIdMax;
    }

    /* renamed from: a */
    public long getSatoshi() {
        TxOut txOut = getPrevTransactionTxOut();
        if (txOut != null) {
            return txOut.getSatoshi();
        }
        return 0;
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj) || !(obj instanceof TxIn)) {
            return false;
        }
        if (!this.mPrevTxOut.equals(((TxIn) obj).mPrevTxOut)) {
            return false;
        }
        return true;
    }

    /* renamed from: h */
    public TxnOutType getTxnOutType() {
        Script crVar = this.mScriptSig;
        if (crVar != null) {
            return crVar.getTxnOutType();
        }
        TxOut o = getPrevTransactionTxOut();
        if (o != null) {
            return o.getTxnOutType();
        }
        return TxnOutType.TX_NONSTANDARD;
    }

    //mo43303q
    public CTxDestination getCTxDestination() {
        TxOut txOut = getPrevTransactionTxOut();
        if (txOut != null) {
            return txOut.getScriptCTxDestination();
        }
        CTxDestination cTxDestination = null;
        CScriptWitness scriptWitness = this.mScriptWitness;
        if (scriptWitness != null && !scriptWitness.isBytesListEmpty()) {
            Iterator bytesListIterator = this.mScriptWitness.stack.iterator();
            while (true) {
                if (!bytesListIterator.hasNext()) {
                    break;
                }
                byte[] bArr = (byte[]) bytesListIterator.next();
                if (bArr != null) {
                    if (bArr.length >= 32 && bArr.length <= 33) {
                        try {
                            CPubKey cPubKey = new CPubKey(bArr);
                            if (cPubKey.isLengthGreaterZero()) {
                                cTxDestination = new WitnessV0KeyHash(cPubKey.getCKeyID());
                                break;
                            }
                        } catch (AddressFormatException e) {
                            e.printStackTrace();
                        }
                    } else if (bArr.length > 72) {
                        try {
                            if (new Script(bArr).getTxnOutType() == TxnOutType.TX_MULTISIG) {
                                cTxDestination = WitnessV0ScriptHash.m546a(bArr);
                                break;
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        Script scriptSig = this.mScriptSig;
        if (scriptSig != null && !scriptSig.isScriptChunkListEmpty()) {
            CTxDestination cTxDestination1 = this.mScriptSig.mo43169j();
            if (cTxDestination1 != null) {
                return (cTxDestination != null && (cTxDestination instanceof WitnessV0ScriptHash)) ? new CScriptID(CScript.m484a(cTxDestination)) : cTxDestination1;
            }
        }
        return cTxDestination;
    }

    /* renamed from: i */
    public String getAddress() {
        CTxDestination q = getCTxDestination();
        if (q == null || (q instanceof CNoDestination)) {
            return null;
        }
        return AddressUtils.getAddressString(q, this.wallet.getChainParams());
    }

    /* renamed from: r */
    public boolean mo43304r() {
        if (isCoinBaseVin()) {
            return true;
        }
        Script crVar = this.mScriptSig;
        if (crVar == null) {
            return false;
        }
        return crVar.mo43176q();
    }

    /* renamed from: s */
    public boolean mo43305s() {
        return this.mSequence == -1;
    }

    //mo43306t
    public int getLength() {
        int length = this.mPrevTxOut.getLength();
        Script scriptSig = this.mScriptSig;
        return (scriptSig != null ? length + scriptSig.getScriptLength() : length + 1) + 4;
    }

    /* renamed from: u */
    public int mo43307u() {
        Script script = this.mScriptSig;
        if (script == null) {
            return 0;
        }
        List l = script.mo43171l();
        if (l == null || l.isEmpty()) {
            return 0;
        }
        return l.size();
    }

    /* renamed from: v */
    public TxIn clone() {
        return new TxIn(this);
    }
}