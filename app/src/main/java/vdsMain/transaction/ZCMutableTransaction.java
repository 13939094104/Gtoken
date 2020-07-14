package vdsMain.transaction;


import vdsMain.DataTypeToolkit;

import java.util.List;
import java.util.Vector;

//bsj
public class ZCMutableTransaction extends CMutableTransaction implements ZTxInterface {

    /* renamed from: h */
    protected long f12233h;

    /* renamed from: i */
    protected long f12234i;

    /* renamed from: j */
    protected List<SpendDescription> f12235j;

    /* renamed from: m */
    protected List<OutputDescription> f12236m;

    /* renamed from: n */
    protected byte[] f12237n;

    public ZCMutableTransaction(Transaction dhVar) {
        super(dhVar);
        if (dhVar instanceof ZTransaction) {
            ZTransaction bsk = (ZTransaction) dhVar;
            this.f12233h = bsk.exp;
            this.f12234i = bsk.vbalance;
            setSpendDescriptionList(bsk.spendDescriptionList, true);
            setOutputDescriptionList(bsk.outputDescriptionList, true);
            this.f12237n = DataTypeToolkit.bytesCopy(bsk.bsig);
        }
    }

    public ZCMutableTransaction(CMutableTransaction dfVar) {
        super(dfVar);
        if (dfVar instanceof ZCMutableTransaction) {
            ZCMutableTransaction bsj = (ZCMutableTransaction) dfVar;
            this.f12233h = bsj.f12233h;
            this.f12234i = bsj.f12234i;
            setSpendDescriptionList(bsj.f12235j, true);
            setOutputDescriptionList(bsj.f12236m, true);
            this.f12237n = DataTypeToolkit.bytesCopy(bsj.f12237n);
        }
    }

    /* renamed from: a */
    public void setSpendDescriptionList(List<SpendDescription> list, boolean z) {
        if (!z) {
            this.f12235j = list;
        } else if (list == null || list.isEmpty()) {
            this.f12235j = null;
        } else {
            List<SpendDescription> list2 = this.f12235j;
            if (list2 == null) {
                this.f12235j = new Vector(list.size());
            } else {
                list2.clear();
            }
            for (SpendDescription brl : list) {
                this.f12235j.add(new SpendDescription(brl));
            }
        }
    }

    /* renamed from: b */
    public void setOutputDescriptionList(List<OutputDescription> list, boolean z) {
        if (!z) {
            this.f12236m = list;
        } else if (list == null || list.isEmpty()) {
            this.f12236m = null;
        } else {
            List<OutputDescription> list2 = this.f12236m;
            if (list2 == null) {
                this.f12236m = new Vector(list.size());
            } else {
                list2.clear();
            }
            for (OutputDescription brj : list) {
                this.f12236m.add(new OutputDescription(brj));
            }
        }
    }

    /* renamed from: a */
    public void mo43034a(SpendDescription brl) {
        if (this.f12235j == null) {
            this.f12235j = new Vector();
        }
        this.f12235j.add(brl);
    }

    /* renamed from: a */
    public void mo43033a(OutputDescription brj) {
        if (this.f12236m == null) {
            this.f12236m = new Vector();
        }
        this.f12236m.add(brj);
    }

    /* renamed from: i */
    public List<OutputDescription> getOutputDescriptionList() {
        return this.f12236m;
    }

    /* renamed from: j */
    public byte[] getBsign() {
        return this.f12237n;
    }

    /* renamed from: x_ */
    public long getExp() {
        return this.f12233h;
    }

    /* renamed from: l */
    public long getVBalance() {
        return this.f12234i;
    }

    /* renamed from: a_ */
    public void setVBalance(long j) {
        this.f12234i = j;
    }

    /* renamed from: d */
    public void mo43039d(long j) {
        this.f12234i += j;
    }

    /* renamed from: m */
    public List<SpendDescription> getSpendDescriptionList() {
        return this.f12235j;
    }

    /* renamed from: e */
    public void setExp(long j) {
        this.f12233h = j;
    }

    /* renamed from: a */
    public void setSigt(byte[] bArr, boolean z) {
        if (!z) {
            this.f12237n = bArr;
            return;
        }
        if (bArr == null) {
            this.f12237n = null;
        } else if (bArr.length == 64) {
            this.f12237n = DataTypeToolkit.bytesCopy(bArr);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("binding_sig_t lengt ");
            sb.append(bArr.length);
            sb.append(" must be ");
            sb.append(64);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    /* renamed from: A_ */
    public boolean isSpendDescriptionListNotEmpty() {
        List<SpendDescription> list = this.f12235j;
        return list != null && !list.isEmpty();
    }

    /* renamed from: B_ */
    public boolean isOutputDescriptionListNotEmpty() {
        List<OutputDescription> list = this.f12236m;
        return list != null && !list.isEmpty();
    }
}