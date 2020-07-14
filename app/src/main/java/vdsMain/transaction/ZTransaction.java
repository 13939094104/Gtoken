package vdsMain.transaction;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import generic.utils.AddressUtils;
import org.apache.commons.io.IOUtils;
import org.spongycastle.apache.bzip2.BZip2Constants;
import vcash.model.VTxModel;
import vdsMain.*;
import vdsMain.block.CValidationState;
import vdsMain.model.AddressModel;
import vdsMain.table.AbstractTable;
import vdsMain.wallet.Wallet;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

//bsk
public abstract class ZTransaction extends Transaction implements ZTxInterface {

    //f12238T
    //910 f12373T
    protected long exp = 0;

    //f12239U
    //910 f12374U
    protected byte[] bsig = null;

    //f12240Y
    //910 f12375Y
    protected List<SpendDescription> spendDescriptionList;

    //f12241Z
    //910 f12376Z
    protected List<OutputDescription> outputDescriptionList;

    //f12242aa
    //910 f12377aa
    protected long vbalance = 0;

    //f12243ab
    //910 f12378ab
    protected MapSaplingNoteDataT mapSaplingNoteDataT;

    //f12244ac
    //910 f12379ac
    protected List<SaplingUtxoValue> saplingUtxoValueList;

    /* access modifiers changed from: protected */
    /* renamed from: Y */
    public abstract boolean mo42819Y();

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public abstract void mo42824a(List<SaplingUtxoValue> list, MapSaplingNoteDataT brr);

    public ZTransaction(@NonNull Wallet izVar) {
        super(izVar);
    }

    public ZTransaction(Transaction dhVar) {
        super(dhVar);
        if (dhVar instanceof ZTransaction) {
            m10547a((ZTransaction) dhVar);
        }
    }

    public ZTransaction(CMutableTransaction dfVar) {
        super(dfVar);
        if (dfVar instanceof ZCMutableTransaction) {
            mo43049a((ZCMutableTransaction) dfVar);
        }
    }

    public ZTransaction(@NonNull Wallet izVar, AbstractTable fxVar) {
        super(izVar, fxVar);
    }

    //mo43043l
    public long getVBalance() {
        return this.vbalance;
    }

    //mo43037a_
    public void setVBalance(long j) {
        this.vbalance = j;
    }

    /* renamed from: d */
    public void mo43039d(long j) {
        this.vbalance += j;
    }

    /* renamed from: a */
    public void mo43034a(SpendDescription brl) {
        if (this.spendDescriptionList == null) {
            this.spendDescriptionList = new Vector();
        }
        this.spendDescriptionList.add(brl);
    }

    /* renamed from: a */
    public void mo43033a(OutputDescription brj) {
        if (this.outputDescriptionList == null) {
            this.outputDescriptionList = new Vector();
        }
        this.outputDescriptionList.add(brj);
    }

    //mo43045x_
    public long getExp() {
        return this.exp;
    }

    //mo43040e
    public void setExp(long j) {
        this.exp = j;
    }

    //mo43042j
    public byte[] getBsign() {
        return this.bsig;
    }

    //mo43036a
    public void setSigt(byte[] bArr, boolean z) {
        if (!z) {
            this.bsig = bArr;
            return;
        }
        if (bArr == null) {
            this.bsig = null;
        } else if (bArr.length == 64) {
            this.bsig = DataTypeToolkit.bytesCopy(bArr);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("binding_sig_t lengt ");
            sb.append(bArr.length);
            sb.append(" must be ");
            sb.append(64);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    //mo43044m
    public List<SpendDescription> getSpendDescriptionList() {
        return this.spendDescriptionList;
    }

    //mo43035a
    public void setSpendDescriptionList(List<SpendDescription> list, boolean z) {
        if (!z) {
            this.spendDescriptionList = list;
            return;
        }
        if (list == null || list.isEmpty()) {
            this.spendDescriptionList = null;
        } else {
            this.spendDescriptionList = new Vector(list);
        }
    }

    //mo43041i
    public List<OutputDescription> getOutputDescriptionList() {
        return this.outputDescriptionList;
    }

    //mo43038b
    public void setOutputDescriptionList(List<OutputDescription> list, boolean z) {
        if (!z) {
            this.outputDescriptionList = list;
            return;
        }
        if (list == null || list.isEmpty()) {
            this.outputDescriptionList = null;
        } else {
            this.outputDescriptionList = new Vector(list);
        }
    }

    public void mo43116af() {
        this.exp = 0;
        this.bsig = null;
        this.spendDescriptionList = null;
        this.outputDescriptionList = null;
        this.vbalance = 0;
        this.mapSaplingNoteDataT = null;
        this.saplingUtxoValueList = null;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo43048a(Cursor cursor, int i) {
        byte[] blob = cursor.getBlob(i);
        if (blob == null || blob.length == 0) {
            this.spendDescriptionList = null;
            return;
        }
        try {
            this.spendDescriptionList = VTransactionSerializer.m10301a(new DummySeriableData(new ByteBuffer(blob)));
        } catch (Exception e) {
            e.printStackTrace();
            this.spendDescriptionList = null;
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo43060b(Cursor cursor, int i) {
        byte[] blob = cursor.getBlob(i);
        if (blob == null || blob.length == 0) {
            this.outputDescriptionList = null;
            return;
        }
        try {
            this.outputDescriptionList = VTransactionSerializer.m10305b(new DummySeriableData(new ByteBuffer(blob)));
        } catch (Exception e) {
            e.printStackTrace();
            this.outputDescriptionList = null;
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo43047a(ContentValues contentValues) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            VTransactionSerializer.writeSpendDescriptionList(this.spendDescriptionList, (StreamWriter) new DummySeriableData((OutputStream) byteArrayOutputStream));
            contentValues.put("sspend", byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo43059b(ContentValues contentValues) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            VTransactionSerializer.writeOutputDescriptionList(this.outputDescriptionList, new DummySeriableData((OutputStream) byteArrayOutputStream));
            contentValues.put("soutput", byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: a */
    public boolean mo43053a(VOutList... mbVarArr) {
        if ((this.getTxInList() == null || this.getTxInList().isEmpty() || mo42833b(mbVarArr)) && mo43054af()) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    /* renamed from: af */
    public boolean mo43054af() {
        return mo43051a(new CValidationState(), ProofVerifier.m10403a());
    }

    /* renamed from: a */
    public boolean mo43051a(CValidationState atVar, ProofVerifier bra) {
        return m10548a(atVar);
    }

    /* renamed from: a */
    private boolean m10548a(CValidationState atVar) {
        if (this.version < -1) {
            return atVar.mo41041a(100, false, 16, "bad-txns-version-too-low");
        }
        if (this.getTxInList().isEmpty() && !isSpendDescriptionListNotEmpty()) {
            return atVar.mo41041a(10, false, 16, "bad-txns-vin-empty");
        }
        if (this.getTxOutList().isEmpty() && !isOutputDescriptionListNotEmpty()) {
            return atVar.mo41041a(10, false, 16, "bad-txns-vout-empty");
        }
        int z = getTransactionBytesLength();
        if (z > 100000) {
            StringBuilder sb = new StringBuilder();
            sb.append("bad-txns-oversize ");
            sb.append(z);
            sb.append(" > ");
            sb.append(BZip2Constants.baseBlockSize);
            return atVar.mo41041a(100, false, 16, sb.toString());
        }
        long j = 0;
        for (TxOut dnVar : this.getTxOutList()) {
            if (dnVar.getSatoshi() < 0) {
                return atVar.mo41041a(100, false, 16, "bad-txns-vout-negative");
            }
            if (dnVar.getSatoshi() > 2100000000000000L) {
                return atVar.mo41041a(100, false, 16, "bad-txns-vout-toolarge");
            }
            j += dnVar.getSatoshi();
            if (!CAmount.m10853b(j)) {
                return atVar.mo41041a(100, false, 16, "bad-txns-txouttotal-toolarge");
            }
        }
        if (this.vbalance != 0 && Collection.m11555a((java.util.Collection) this.spendDescriptionList) && Collection.m11555a((java.util.Collection) this.outputDescriptionList)) {
            return atVar.mo41041a(100, false, 16, "bad-txns-valuebalance-nonzero");
        }
        long j2 = this.vbalance;
        if (j2 > 2100000000000000L || j2 < -2100000000000000L) {
            return atVar.mo41041a(100, false, 16, "bad-txns-valuebalance-toolarge");
        }
        List<SpendDescription> list = this.spendDescriptionList;
        if (list != null && !list.isEmpty()) {
            HashMap hashMap = new HashMap();
            for (SpendDescription brl : this.spendDescriptionList) {
                if (hashMap.containsKey(brl.nullifier)) {
                    return atVar.mo41041a(100, false, 16, "bad-spend-description-nullifiers-duplicate");
                }
                hashMap.put(brl.nullifier, Boolean.valueOf(true));
            }
        }
        if (isCoinBaseTransaction()) {
            if (!Collection.m11555a((java.util.Collection) this.spendDescriptionList)) {
                return atVar.mo41041a(100, false, 16, "bad-cb-has-spend-description");
            }
            if (!Collection.m11555a((java.util.Collection) this.outputDescriptionList)) {
                return atVar.mo41041a(100, false, 16, "bad-cb-has-output-description");
            }
        }
        return true;
    }

    /* renamed from: ag */
    public boolean mo43055ag() {
        MapSaplingNoteDataT brr = this.mapSaplingNoteDataT;
        boolean z = false;
        boolean z2 = brr != null && !brr.isEmpty();
        AddressModel D = this.wallet.getSelfAddressModel();
        List<SaplingUtxoValue> list = this.saplingUtxoValueList;
        if (list != null && !list.isEmpty()) {
            for (SaplingUtxoValue bpn : this.saplingUtxoValueList) {
                if (bpn.cTxDestination != null && D.isUsingDesAddressMapHasKey(bpn.cTxDestination)) {
                    return true;
                }
            }
        }
        if (z2 || mo42819Y()) {
            z = true;
        }
        return z;
    }

    /* renamed from: y_ */
    public boolean isRelatedToLocalAddress() {
        return super.isRelatedToLocalAddress() || mo43055ag();
    }

    /* renamed from: z_ */
    public boolean isFromLocalAddress() {
        return super.isFromLocalAddress() || mo42819Y();
    }

    /* renamed from: b */
    public void fillDesListWithRelatedAddress(List<CTxDestination> list) {
        super.fillDesListWithRelatedAddress(list);
        mo43062c(list);
    }

    /* renamed from: c */
    public void mo43062c(List<CTxDestination> list) {
        if (list != null) {
            List<SaplingUtxoValue> list2 = this.saplingUtxoValueList;
            if (list2 != null && !list2.isEmpty()) {
                for (SaplingUtxoValue bpn : this.saplingUtxoValueList) {
                    if (!list.contains(bpn.cTxDestination)) {
                        list.add(bpn.cTxDestination);
                    }
                }
            }
            List<SpendDescription> list3 = this.spendDescriptionList;
            if (list3 != null && !list3.isEmpty()) {
                VTxModel vTxModel = (VTxModel) this.wallet.getSelfTransactionModel();
                for (SpendDescription brl : this.spendDescriptionList) {
                    SaplingUtxoValue l = vTxModel.mo40464l(brl.nullifier);
                    if (!(l == null || l.cTxDestination == null || this.wallet.getAddressByCTxDestinationFromArrayMap(l.cTxDestination) == null || list.contains(l.cTxDestination))) {
                        list.add(l.cTxDestination);
                    }
                }
            }
        }
    }

    /* renamed from: a */
    public SaplingUtxoValue mo43046a(SaplingOutpoint brk) {
        List<SaplingUtxoValue> list = this.saplingUtxoValueList;
        if (list == null || list.isEmpty()) {
            return null;
        }
        for (SaplingUtxoValue bpn : this.saplingUtxoValueList) {
            if (bpn.saplingOutpoint.equals(brk)) {
                return bpn;
            }
        }
        return null;
    }

    /* renamed from: e */
    public SaplingUtxoValue mo43063e(int i) {
        List<SaplingUtxoValue> list = this.saplingUtxoValueList;
        if (list == null || list.isEmpty()) {
            return null;
        }
        for (SaplingUtxoValue bpn : this.saplingUtxoValueList) {
            if (bpn.saplingOutpoint.index == i) {
                return bpn;
            }
        }
        return null;
    }

    //mo43056ah
    public MapSaplingNoteDataT getMapSaplingNoteDataT() {
        return this.mapSaplingNoteDataT;
    }

    /* renamed from: a */
    private void m10547a(ZTransaction zTransaction) {
        this.exp = zTransaction.exp;
        this.vbalance = zTransaction.vbalance;
        setSpendDescriptionList(zTransaction.spendDescriptionList, true);
        setOutputDescriptionList(zTransaction.outputDescriptionList, true);
        setSigt(zTransaction.bsig, true);
        MapSaplingNoteDataT brr = zTransaction.mapSaplingNoteDataT;
        if (brr == null || brr.isEmpty()) {
            this.mapSaplingNoteDataT = null;
        } else {
            MapSaplingNoteDataT brr2 = this.mapSaplingNoteDataT;
            if (brr2 == null) {
                this.mapSaplingNoteDataT = new MapSaplingNoteDataT(zTransaction.mapSaplingNoteDataT);
            } else {
                brr2.clear();
                this.mapSaplingNoteDataT.putAll(zTransaction.mapSaplingNoteDataT);
            }
        }
        List<SaplingUtxoValue> list = zTransaction.saplingUtxoValueList;
        if (list == null || list.isEmpty()) {
            this.saplingUtxoValueList = null;
            return;
        }
        List<SaplingUtxoValue> list2 = this.saplingUtxoValueList;
        if (list2 == null) {
            this.saplingUtxoValueList = new Vector(zTransaction.saplingUtxoValueList);
            return;
        }
        list2.clear();
        this.saplingUtxoValueList.addAll(zTransaction.saplingUtxoValueList);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo43049a(ZCMutableTransaction bsj) {
        this.exp = bsj.f12233h;
        this.vbalance = bsj.f12234i;
        setSpendDescriptionList(bsj.f12235j, true);
        setOutputDescriptionList(bsj.f12236m, true);
        setSigt(bsj.f12237n, true);
        checkAndAddSaplingUtxoValue();
    }

    //mo43057ai
    //910  mo43120aj
    public void checkAndAddSaplingUtxoValue() {
        List<OutputDescription> list = this.outputDescriptionList;
        if (list == null || list.isEmpty()) {
            this.mapSaplingNoteDataT = null;
            this.saplingUtxoValueList = null;
            return;
        }
        if (this.saplingUtxoValueList == null) {
            this.saplingUtxoValueList = new Vector();
        }
        if (this.mapSaplingNoteDataT == null) {
            this.mapSaplingNoteDataT = new MapSaplingNoteDataT();
        }
        mo42824a(this.saplingUtxoValueList, this.mapSaplingNoteDataT);
        if (this.saplingUtxoValueList.isEmpty() || this.mapSaplingNoteDataT.isEmpty()) {
            this.saplingUtxoValueList = null;
            this.mapSaplingNoteDataT = null;
        } else {
            for (SaplingUtxoValue saplingUtxoValue : this.saplingUtxoValueList) {
                this.satoshi+=saplingUtxoValue.value;
            }
        }
    }

    //mo43058aj
    public List<SaplingUtxoValue> getSaplingUtxoValueList() {
        return this.saplingUtxoValueList;
    }

    /* renamed from: n */
    public long mo43064n() {
        return this.satoshi + this.vbalance;
    }

    /* renamed from: c */
    public ContentValues getContentValues() {
        ContentValues c = super.getContentValues();
        c.put("exp", Long.valueOf(this.exp));
        c.put("vbalance", Long.valueOf(this.vbalance));
        mo43047a(c);
        mo43059b(c);
        byte[] bArr = this.bsig;
        if (bArr != null) {
            c.put("bsig", bArr);
        }
        return c;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(super.toString());
        mo43050a(stringBuffer);
        return stringBuffer.toString();
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo43050a(StringBuffer stringBuffer) {
        stringBuffer.append("\nexpiryheight: ");
        stringBuffer.append(this.exp);
        stringBuffer.append("\nvaluebalance: ");
        stringBuffer.append(this.vbalance);
        if (this.bsig != null) {
            stringBuffer.append("\nbinding_sig: ");
            stringBuffer.append(StringToolkit.bytesToString(this.bsig));
        }
        stringBuffer.append("\nvShieldedSpend: \n[");
        List<SpendDescription> list = this.spendDescriptionList;
        if (list != null && !list.isEmpty()) {
            for (SpendDescription brl : this.spendDescriptionList) {
                stringBuffer.append("\n [\n");
                stringBuffer.append(brl.toString());
                stringBuffer.append("\n]");
            }
        }
        stringBuffer.append("\n]");
        stringBuffer.append("\nvShieldedOutput: \n[");
        List<OutputDescription> list2 = this.outputDescriptionList;
        if (list2 != null && !list2.isEmpty()) {
            for (OutputDescription brj : this.outputDescriptionList) {
                stringBuffer.append("\n [\n");
                stringBuffer.append(brj.toString());
                stringBuffer.append("\n]");
            }
        }
        stringBuffer.append("\n]");
        stringBuffer.append("\nsapling output: \n[");
        List<SaplingUtxoValue> list3 = this.saplingUtxoValueList;
        if (list3 != null && !list3.isEmpty()) {
            for (SaplingUtxoValue bpn : this.saplingUtxoValueList) {
                stringBuffer.append(IOUtils.LINE_SEPARATOR_UNIX);
                stringBuffer.append(bpn.saplingOutpoint.toString());
                stringBuffer.append(" ---> ");
                stringBuffer.append(bpn.value);
                stringBuffer.append(" -----> ");
                stringBuffer.append(AddressUtils.getAddressString(bpn.cTxDestination, mo44660R().getChainParams()));
            }
        }
        stringBuffer.append("\n]");
    }

    /* renamed from: a */
    public boolean recomputeByTxs(List<CTxDestination> list) {
        boolean a = super.recomputeByTxs(list);
        checkAndAddSaplingUtxoValue();
        mo43062c(list);
        return a;
    }

    //mo43031A_
    public boolean isSpendDescriptionListNotEmpty() {
        List<SpendDescription> list = this.spendDescriptionList;
        return list != null && !list.isEmpty();
    }

    //mo43032B_
    public boolean isOutputDescriptionListNotEmpty() {
        List<OutputDescription> list = this.outputDescriptionList;
        return list != null && !list.isEmpty();
    }
}
