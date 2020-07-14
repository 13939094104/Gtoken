package vdsMain.transaction;

import androidx.annotation.NonNull;
import bitcoin.script.CScript;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.List;

public class CTransactionSignatureSerializer {

    /* renamed from: a */
    protected TransactionInteface f12276a;

    /* renamed from: b */
    protected CScript f12277b;

    /* renamed from: c */
    protected int f12278c;

    /* renamed from: d */
    protected boolean f12279d;

    /* renamed from: e */
    protected boolean f12280e;

    /* renamed from: f */
    protected boolean f12281f;

    /* renamed from: g */
    protected Wallet f12282g;

    public CTransactionSignatureSerializer(@NonNull Wallet izVar, TransactionInteface diVar, CScript cScript, int i, int i2) {
        this.f12282g = izVar;
        this.f12276a = diVar;
        this.f12277b = cScript;
        this.f12278c = i;
        boolean z = true;
        this.f12279d = (i2 & 128) != 0;
        int i3 = i2 & 31;
        this.f12280e = i3 == 3;
        if (i3 != 2) {
            z = false;
        }
        this.f12279d = z;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo43143a(CHashWriter kVar) throws IOException {
        CScript.ScriptByteIterator d = this.f12277b.getNewScriptByteIterator();
        CScript.ScriptByteIterator a = d.clone();
        ScriptChunk csVar = new ScriptChunk();
        long j = 0;
        while (this.f12277b.GetOp(d, csVar, false)) {
            if (csVar.opWord == 171) {
                j++;
            }
        }
        kVar.writeVariableInt(((long) this.f12277b.getArrayBufferWritePos()) - j);
        d.mo9544a(a);
        while (this.f12277b.GetOp(d, csVar, false)) {
            if (csVar.opWord == 171) {
                kVar.write(a.mo9551e(), a.getPointedIndex(), (d.getPointedIndex() - a.getPointedIndex()) - 1);
                a.mo9544a(d);
            }
        }
        if (a.hasNext()) {
            kVar.write(a.mo9551e(), a.getPointedIndex(), d.getPointedIndex() - a.getPointedIndex());
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo43144a(CHashWriter kVar, int i) throws IOException {
        if (i != VTransaction.uIntMax) {
            if (this.f12279d) {
                i = this.f12278c;
            }
            List e = this.f12276a.getSelfTxInList();
            ((TxIn) e.get(i)).getPrevTxOut().serialToStream((StreamWriter) kVar);
            if (i != this.f12278c) {
                new Script().serialToStream((StreamWriter) kVar);
            } else {
                mo43143a(kVar);
            }
            if (i == this.f12278c || (!this.f12280e && !this.f12281f)) {
                kVar.writeUInt32T(((TxIn) e.get(i)).getSequence());
            } else {
                kVar.writeUInt32T(0);
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("nInput can not be ");
            sb.append(VTransaction.uIntMax);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo43145b(CHashWriter kVar, int i) throws IOException {
        if (!this.f12280e || i == this.f12278c) {
            ((TxOut) this.f12276a.getSelfTxOutList().get(i)).serialToStream((StreamWriter) kVar);
        } else {
            this.f12282g.getSelfWalletHelper().getNewTxOut().serialToStream((StreamWriter) kVar);
        }
    }

    /* renamed from: b */
    public void mo42757b(CHashWriter kVar) throws IOException {
        kVar.writeUInt32T(this.f12276a.getVersion());
        mo43146c(kVar);
        mo43147d(kVar);
        kVar.writeUInt32T(this.f12276a.getLockTime());
    }

    /* access modifiers changed from: protected */
    /* renamed from: c */
    public void mo43146c(CHashWriter kVar) throws IOException {
        int size = this.f12279d ? 1 : this.f12276a.getSelfTxInList().size();
        kVar.writeVariableInt((long) size);
        for (int i = 0; i < size; i++) {
            mo43144a(kVar, i);
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: d */
    public void mo43147d(CHashWriter kVar) throws IOException {
        int i = this.f12281f ? 0 : this.f12280e ? this.f12278c + 1 : this.f12276a.getSelfTxOutList().size();
        kVar.writeVariableInt((long) i);
        for (int i2 = 0; i2 < i; i2++) {
            mo43145b(kVar, i2);
        }
    }
}
