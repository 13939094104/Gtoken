package vdsMain.transaction;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.NonNull;
import bitcoin.CKeyStore;
import bitcoin.UInt256;
import bitcoin.VariableInteger;
import bitcoin.script.BaseSignatureChecker;
import bitcoin.script.CScript;
import com.vc.libcommon.exception.AddressFormatException;
import generic.exceptions.InvalidateTransactionException;
import generic.exceptions.NotMatchException;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import net.bither.bitherj.exception.ScriptException;
import org.jetbrains.annotations.NotNull;
import vcash.model.VTxModel;
import vcash.script.VTransactionSignatureChecker;
import vdsMain.*;
import vdsMain.table.AbstractTable;
import vdsMain.wallet.Wallet;
import vdsMain.model.Address;
import java.io.IOException;
import java.util.*;
import vdsMain.tool.Long;
//bqf
//910 bqu
public class VTransaction extends  ZTransaction  implements VTransactionInterface{
    public VTransaction(@NonNull Wallet izVar) {
        super(izVar);
    }

    public VTransaction(CMutableTransaction dfVar) {
        super(dfVar);
    }

    public VTransaction(Transaction transaction) {
        super(transaction);
        if (transaction != null && (transaction instanceof VTransaction)) {
            VTransaction vTransaction = (VTransaction) transaction;
        }
    }

    public VTransaction(@NonNull Wallet izVar, AbstractTable fxVar) {
        super(izVar, fxVar);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void writeSerialData(StreamWriter streamWriter) {
        try {
            VTransactionSerializer.writeVTransactionToStreamWriter((VTransactionInterface) this, streamWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void decodeSerialData(SeriableData seriableData) {
        VTransactionSerializer.decodeFromSeriableData((VTransactionInterface) this, seriableData);
    }

    /* renamed from: ae */
    private int mo42882ae() {
        int i;
        int i2;
        if (this.spendDescriptionList == null || this.spendDescriptionList.isEmpty()) {
            i = VariableInteger.getLengthNative(0) + 12;
        } else {
            i = VariableInteger.getLengthNative((long) this.spendDescriptionList.size()) + 12 + this.spendDescriptionList.size() + 384;
        }
        if (this.outputDescriptionList == null || this.outputDescriptionList.isEmpty()) {
            i2 = i + VariableInteger.getLengthNative(0);
        } else {
            i2 = i + VariableInteger.getLengthNative((long) this.outputDescriptionList.size()) + this.outputDescriptionList.size() + 948;
        }
        return i2 + 64;
    }

    /* renamed from: z */
    public int getTransactionBytesLength() {
        return super.getTransactionBytesLength() + mo42882ae();
    }

    /* renamed from: c */
    public ContentValues getContentValues() {
        return super.getContentValues();
    }

    /* renamed from: a */
    public void initTableItemVariable(Cursor cursor, int i, int i2, int i3) {
        switch (i3) {
            case 10:
                this.exp = cursor.getLong(i3);
                break;
            case 11:
                this.vbalance = cursor.getLong(i3);
                break;
            case 12:
                break;
            case 13:
                mo43048a(cursor, i3);
                break;
            case 14:
                mo43060b(cursor, i3);
                break;
            case 15:
                this.bsig = cursor.getBlob(i3);
                if (this.bsig != null) {
                    if (this.bsig.length == 0) {
                        this.bsig = null;
                        break;
                    } else if (this.bsig.length != 64) {
                        try {
                            throw new InvalidateTransactionException(String.format(Locale.getDefault(), "Invalidate binding sig length: %d", new Object[]{Integer.valueOf(this.bsig.length)}));
                        } catch (InvalidateTransactionException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case 16:
                if (isOutputDescriptionListNotEmpty()) {
                    byte[] blob = cursor.getBlob(i3);
                    if (!(blob == null || blob.length == 0)) {
                        SerializedSaplingNoteMap bjx = new SerializedSaplingNoteMap();
                        try {
                            bjx.decodeSerialStream(blob, 0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        this.mapSaplingNoteDataT = new MapSaplingNoteDataT();
                        this.saplingUtxoValueList = new ArrayList();
                        bjx.mo42527a(getTxId(), this.wallet, this.saplingUtxoValueList, this.mapSaplingNoteDataT);
                        if (!this.saplingUtxoValueList.isEmpty() && !this.mapSaplingNoteDataT.isEmpty()) {
                            mo42818S();
                            break;
                        } else {
                            this.saplingUtxoValueList = null;
                            this.mapSaplingNoteDataT = null;
                            return;
                        }
                    }
                }
                break;
            default:
                super.initTableItemVariable(cursor, i, i2, i3);
                break;
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: S */
    public void mo42818S() {
        if (this.saplingUtxoValueList != null && !this.saplingUtxoValueList.isEmpty() && this.mapSaplingNoteDataT != null && !this.mapSaplingNoteDataT.isEmpty()) {
            for (SaplingUtxoValue bpn : this.saplingUtxoValueList) {
                Address b = this.wallet.getAddressByCTxDestinationFromArrayMap(bpn.cTxDestination);
                if (b != null) {
                    bpn.cTxDestination = b.getCTxDestination();
                }
            }
            SaplingIncomingViewingKeyMap c = ((VCCryptoKeyStore) this.wallet.getSelfCWallet().getCKeyStore()).mo42934c();
            for (Map.Entry entry : this.mapSaplingNoteDataT.entrySet()) {
                ((SaplingNoteData) entry.getValue()).incomingViewingKey = (SaplingIncomingViewingKey) c.get(mo43063e(((SaplingOutpoint) entry.getKey()).index).cTxDestination);
            }
        }
    }

    /* renamed from: a */
    public boolean signTransaction(CharSequence pwd) throws NotMatchException {
        android.util.Log.d("VTransaction", "signTransaction");
        this.wallet.checkWalletPassword(pwd);
        boolean isSuccess = mo43255b(pwd);
        updateTxidByContent();
        recomputeByTxs(null);
        return isSuccess;
    }

    /* renamed from: a */
    public boolean mo42826a(CharSequence charSequence, VOutList mbVar, ScriptList lvVar) throws NotMatchException, AddressFormatException, ScriptException {
        boolean z;
        this.wallet.checkWalletPassword(charSequence);
        if (mbVar == null || mbVar.mo44704a() == 0) {
            z = mo43255b(charSequence);
        } else {
            z = signrawtransaction(charSequence, mbVar, lvVar);
        }
        updateTxidByContent();
        recomputeByTxs(null);
        return z;
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public boolean mo42833b(VOutList... mbVarArr) {
        VOutList[] mbVarArr2 = mbVarArr;
        if (this.getTxInList().isEmpty()) {
            return true;
        }
        VSign bps = new VSign();
        Iterator c = (mbVarArr2.length <= 0 || mbVarArr2[0].isVOutListEmpty() || mbVarArr2[0].mo44704a() != this.getTxInList().size()) ? null : mbVarArr2[0].getTxOutIterator();
        int i = 0;
        for (TxIn dlVar : this.getTxInList()) {
            if (!dlVar.mo43304r()) {
                TxOut o = c != null ? (TxOut) c.next() : dlVar.getPrevTransactionTxOut();
                if (o == null) {
                    return false;
                }
                ScriptErrorResult cuVar = new ScriptErrorResult();
                try {
                    if (!bps.getSelfInterpreter().VerifyScript(new CScript((SeriableData) dlVar.getScriptSig()), new CScript((SeriableData) o.getScript()), new CScriptWitness(dlVar.getCScriptWitness()), 131039, (BaseSignatureChecker) new VTransactionSignatureChecker(this, i), cuVar) && cuVar.scriptError != ScriptError.SCRIPT_ERR_OK) {
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            i++;
        }
        return true;
    }

    //TODO 签名，不知道哪里错了
    /* renamed from: b */
    public boolean signrawtransaction(CharSequence pwd, VOutList preVoutList, ScriptList scriptList) throws AddressFormatException, ScriptException {
        SignatureData txInSignatureData;
        TxIn txIn;
        CScript prevPubKey;
        int i;
        int zeroIndex = 0;
        if (preVoutList != null && !preVoutList.isVOutListEmpty()) {
            Iterator preVOutIterator = preVoutList.getTxOutIterator();
            VSign vSign = new VSign();
            VCMutableTransaction vcMutableTransaction = new VCMutableTransaction((Transaction) this);
            Transaction cloneTransaction = clone();
            CKeyStore cKeyStore = this.wallet.getSelfCWallet().getCKeyStore();
            int index = 0;
            boolean isSuccess = true;
            for (TxIn txIn1 : this.getTxInList()) {
                CScript preVoutCScript = new CScript((SeriableData) ((TxOut) preVOutIterator.next()).getScript());
                SignatureData signatureData1 = new SignatureData();
                if (scriptList == null || index >= scriptList.getScriptListSize()) {
                    txInSignatureData = signatureData1;
                    prevPubKey = preVoutCScript;
                    txIn = txIn1;
                    int[] iArr = new int[1];
                    iArr[zeroIndex] = 1;
                    CScript[] cScriptArr = new CScript[zeroIndex];
                    i = index;
                    vSign.ProduceSignature(pwd, new VMutableTransactionSignatureCreator(cKeyStore, vcMutableTransaction, index, iArr), prevPubKey, txInSignatureData, cScriptArr);
                } else {
                    int[] iArr2 = new int[1];
                    iArr2[zeroIndex] = 1;
                    VMutableTransactionSignatureCreator vMutableTransactionSignatureCreator = new VMutableTransactionSignatureCreator(cKeyStore, vcMutableTransaction, index, iArr2);
                    CScript[] cScriptArr2 = new CScript[1];
                    cScriptArr2[zeroIndex] = scriptList.mo44671a(index);
                    txInSignatureData = signatureData1;
                    prevPubKey = preVoutCScript;
                    txIn = txIn1;
                    vSign.ProduceSignature(pwd, vMutableTransactionSignatureCreator, preVoutCScript, txInSignatureData, cScriptArr2);
                    i = index;
                }
                SignatureData sigdata = vSign.CombineSignatures(prevPubKey, (BaseSignatureChecker) new VTransactionSignatureChecker(cloneTransaction, i), txInSignatureData, vSign.DataFromTransaction((CMutableTransaction) vcMutableTransaction, (long) i));
               //应用了签名
                Sign.UpdateTransaction(txIn, sigdata);
                ScriptErrorResult scriptErrorResult = new ScriptErrorResult();
                if (!vSign.getSelfInterpreter().VerifyScript(sigdata.cScript, prevPubKey, txIn.getCScriptWitness(), 131039, (BaseSignatureChecker) new VTransactionSignatureChecker(cloneTransaction, i), scriptErrorResult)
                        && scriptErrorResult.scriptError != ScriptError.SCRIPT_ERR_OK) {
                    Log.logObjectWarning((Object) this, String.format(Locale.getDefault(), "Sign vin %d failed %s", new Object[]{Integer.valueOf(i), scriptErrorResult.scriptError.toString()}));
                    isSuccess = false;
                }
                index = i + 1;
                zeroIndex = 0;
            }
            return isSuccess;
        } else if (this.spendDescriptionList != null && !this.spendDescriptionList.isEmpty()) {
            return true;
        } else {
            Log.logObjectWarning((Object) this, "Previors vouts is empty!");
            return false;
        }
    }

    /* renamed from: a */
    public long getVoutSumSubVinSum(CTxDestination oVar) {
        if (oVar instanceof SaplingPaymentAddress) {
            return mo42821a((SaplingPaymentAddress) oVar);
        }
        return super.getVoutSumSubVinSum(oVar);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public long mo42821a(SaplingPaymentAddress bsf) {
        if (bsf == null) {
            return 0;
        }
        Long grVar = new Long(0);
        Long grVar2 = new Long(0);
        mo42823a(bsf, false, grVar);
        mo42831b(bsf, false, grVar2);
        grVar2.mo43678b(grVar);
        return grVar2.longValue();
    }

    /* renamed from: a */
    public List<SaplingUtxoValue> mo42823a(SaplingPaymentAddress bsf, boolean z, Long... grVarArr) {
        Vector vector = null;
        if (this.spendDescriptionList == null || this.spendDescriptionList.isEmpty()) {
            return null;
        }
        if (z) {
            vector = new Vector();
        }
        VTxModel vTxModel = (VTxModel) this.wallet.getSelfTransactionModel();
        for (SpendDescription a : this.spendDescriptionList) {
            SaplingUtxoValue l = vTxModel.mo40464l(a.mo42947a());
            if (l != null) {
                if (bsf == null) {
                    if (z) {
                        vector.add(l);
                    }
                    if (grVarArr.length > 0) {
                        grVarArr[0].mo43680c(l.value);
                    }
                } else if (l.cTxDestination.equals(bsf)) {
                    if (z) {
                        vector.add(l);
                    }
                    if (grVarArr.length > 0) {
                        grVarArr[0].mo43680c(l.value);
                    }
                }
            }
        }
        return vector;
    }

    /* renamed from: b */
    public List<SaplingUtxoValue> mo42831b(SaplingPaymentAddress bsf, boolean z, Long... grVarArr) {
        Vector vector = null;
        if (this.saplingUtxoValueList == null || this.saplingUtxoValueList.isEmpty()) {
            return null;
        }
        if (z) {
            vector = new Vector();
        }
        VTxModel vTxModel = (VTxModel) this.wallet.getSelfTransactionModel();
        for (SaplingUtxoValue bpn : this.saplingUtxoValueList) {
            if (bsf == null) {
                if (z) {
                    vector.add(bpn);
                }
                if (grVarArr.length > 0) {
                    grVarArr[0].mo43680c(bpn.value);
                }
            } else if (bpn.cTxDestination.equals(bsf)) {
                if (z) {
                    vector.add(bpn);
                }
                if (grVarArr.length > 0) {
                    grVarArr[0].mo43680c(bpn.value);
                }
            }
        }
        return vector;
    }

    //mo42816i_
    @NotNull
    public UInt256 updateTxidByContent() {
        android.util.Log.d("VTransaction", "updateTxidByContent");
        this.getTxid().clear();
        try {
            CHashWriter cHashWriter = new CHashWriter();
            VTransactionSerializer.writeVTransactionToStreamWriter((VTransactionInterface) this, (StreamWriter) cHashWriter);
            setTxid(cHashWriter.GetHash());
        } catch (IOException e) {
            e.printStackTrace();
            setTxid(UInt256.empty());
        }
        return this.getTxid();
    }

    /* access modifiers changed from: protected */
    /* renamed from: Y */
    public boolean mo42819Y() {
        VTxModel vTxModel = (VTxModel) this.wallet.getSelfTransactionModel();
        if (this.spendDescriptionList != null && !this.spendDescriptionList.isEmpty()) {
            for (SpendDescription spendDescription : this.spendDescriptionList) {
                if (!vTxModel.mo40461i(spendDescription.nullifier)) {
                    if (vTxModel.mo40462j(spendDescription.nullifier)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42824a(List<SaplingUtxoValue> list, MapSaplingNoteDataT brr) {
        ((VTxModel) this.wallet.getSelfTransactionModel()).mo40443a((Transaction) this, list, brr);
    }

    /* renamed from: P */
    public ContentValues getBlockHashAndNoContentValues() {
        ContentValues P = super.getBlockHashAndNoContentValues();
        if (!(this.saplingUtxoValueList == null || this.mapSaplingNoteDataT == null)) {
            try {
                SerializedSaplingNoteMap bjx = new SerializedSaplingNoteMap();
                bjx.addToIndexToSaplingNoteItemMap(this.saplingUtxoValueList, this.mapSaplingNoteDataT);
                byte[] serialToStream = bjx.serialToStream();
                bjx.decodeSerialStream(serialToStream, 0);
                P.put("sapn", serialToStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return P;
    }

    /* renamed from: Z */
    public boolean mo42820Z() {
        boolean z = false;
        if (this.getTxOutList() == null || this.getTxOutList().isEmpty()) {
            return false;
        }
        Script j = getTxOut(0).getScript();
        if (j != null && j.isTxContractType()) {
            z = true;
        }
        return z;
    }

    /* renamed from: aa */
    public boolean mo42827aa() {
        int i = 0;
        if (mo42820Z()) {
            Script j = getTxOut(0).getScript();
            if (j.getTxnOutType() == TxnOutType.TX_CONTRACT_CREATE) {
                byte[] bArr = ((ScriptChunk) j.mo43161d().get(3)).bytes;
                if (bArr.length > GroupUtil.f11797a.length) {
                    while (i < GroupUtil.f11797a.length && bArr[i] == GroupUtil.f11797a[i]) {
                        i++;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /* renamed from: ab */
    public boolean mo42828ab() {
        int i = 0;
        if (mo42820Z()) {
            Script j = getTxOut(0).getScript();
            if (j.getTxnOutType() == TxnOutType.TX_CONTRACT_CALL) {
                byte[] bArr = ((ScriptChunk) j.mo43161d().get(3)).bytes;
                if (bArr.length == GroupUtil.f11798b.length) {
                    while (i < GroupUtil.f11798b.length && bArr[i] == GroupUtil.f11798b[i]) {
                        i++;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /* renamed from: ac */
    public String mo42829ac() {
        if (!mo42827aa() && !mo42828ab()) {
            return null;
        }
        Script j = getTxOut(0).getScript();
        if (j.getTxnOutType() == TxnOutType.TX_CONTRACT_CREATE) {
            return GroupUtil.m9748a(this);
        }
        return StringToolkit.bytesToString(((ScriptChunk) j.mo43161d().get(4)).bytes).toLowerCase();
    }

    @NotNull
    @Override
    public String toString() {
        return super.toString();
    }

    //910 mo42892ad
    //mo42830ad
    public boolean isSpendDesciptionListAndOuputDescriptionListNotEmpty() {
        if (this.spendDescriptionList != null && !this.spendDescriptionList.isEmpty()) {
            return true;
        }
        if (this.outputDescriptionList == null || this.outputDescriptionList.isEmpty()) {
            return false;
        }
        return true;
    }
}
