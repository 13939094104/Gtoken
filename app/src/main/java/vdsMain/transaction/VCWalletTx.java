package vdsMain.transaction;

import androidx.annotation.NonNull;
import android.util.Pair;
import com.vc.libcommon.util.UInt;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.table.AbstractTable;
import vdsMain.wallet.CWallet;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

//bqq
public class VCWalletTx extends VCMerkleTx {

    /* renamed from: A */
    public boolean f12094A;

    /* renamed from: B */
    public boolean f12095B;

    /* renamed from: C */
    public boolean f12096C;

    /* renamed from: D */
    public boolean f12097D;

    /* renamed from: E */
    public boolean f12098E;

    /* renamed from: F */
    public boolean f12099F;

    /* renamed from: G */
    public boolean f12100G;

    /* renamed from: H */
    public boolean f12101H;

    /* renamed from: I */
    public boolean f12102I;

    /* renamed from: J */
    public long f12103J;

    /* renamed from: K */
    public long f12104K;

    /* renamed from: L */
    public long f12105L;

    /* renamed from: M */
    public long f12106M;

    /* renamed from: N */
    public long f12107N;

    /* renamed from: O */
    public long f12108O;

    /* renamed from: P */
    public long f12109P;

    /* renamed from: Q */
    public long f12110Q;

    /* renamed from: R */
    public long f12111R;

    /* renamed from: S */
    public long f12112S = -1;

    /* renamed from: t */
    public HashMap<String, String> f12113t = new HashMap<>();

    /* renamed from: u */
    public Vector<Pair<String, String>> f12114u = new Vector<>();

    /* renamed from: v */
    public long f12115v;

    /* renamed from: w */
    public long f12116w;

    /* renamed from: x */
    public long f12117x;

    /* renamed from: y */
    public boolean f12118y;

    /* renamed from: z */
    public String f12119z;

    public VCWalletTx(@NonNull Wallet izVar) {
        super(izVar);
    }

    public VCWalletTx(Transaction transaction) {
        super(transaction);
        if (transaction != null && (transaction instanceof VCWalletTx)) {
            m10348a((VCWalletTx) transaction);
        }
    }

    public VCWalletTx(@NonNull Wallet izVar, AbstractTable fxVar) {
        super(izVar, fxVar);
    }

    /* renamed from: a */
    private void m10348a(VCWalletTx vcWalletTx) {
        this.f12113t.clear();
        this.f12113t.putAll(vcWalletTx.f12113t);
        this.f12114u.clear();
        this.f12114u.addAll(vcWalletTx.f12114u);
        this.f12115v = vcWalletTx.f12115v;
        this.f12116w = vcWalletTx.f12116w;
        this.f12117x = vcWalletTx.f12117x;
        this.f12118y = vcWalletTx.f12118y;
        this.f12119z = vcWalletTx.f12119z;
        this.f12094A = vcWalletTx.f12094A;
        this.f12095B = vcWalletTx.f12095B;
        this.f12096C = vcWalletTx.f12096C;
        this.f12097D = vcWalletTx.f12097D;
        this.f12098E = vcWalletTx.f12098E;
        this.f12099F = vcWalletTx.f12099F;
        this.f12100G = vcWalletTx.f12100G;
        this.f12101H = vcWalletTx.f12101H;
        this.f12102I = vcWalletTx.f12102I;
        this.f12103J = vcWalletTx.f12103J;
        this.f12104K = vcWalletTx.f12104K;
        this.f12105L = vcWalletTx.f12105L;
        this.f12106M = vcWalletTx.f12106M;
        this.f12107N = vcWalletTx.f12107N;
        this.f12108O = vcWalletTx.f12108O;
        this.f12109P = vcWalletTx.f12109P;
        this.f12110Q = vcWalletTx.f12110Q;
        this.f12111R = vcWalletTx.f12111R;
        this.f12112S = vcWalletTx.f12112S;
    }

    /* renamed from: c */
    private void m10349c(CWallet mfVar) {
        this.f12113t.clear();
        this.f12114u.clear();
        this.f12115v = 0;
        this.f12116w = 0;
        this.f12117x = 0;
        this.f12118y = false;
        this.f12119z = null;
        this.f12094A = false;
        this.f12095B = false;
        this.f12096C = false;
        this.f12097D = false;
        this.f12098E = false;
        this.f12099F = false;
        this.f12100G = false;
        this.f12101H = false;
        this.f12102I = false;
        this.f12103J = 0;
        this.f12104K = 0;
        this.f12105L = 0;
        this.f12106M = 0;
        this.f12107N = 0;
        this.f12108O = 0;
        this.f12110Q = 0;
        this.f12109P = 0;
        this.f12111R = 0;
        this.f12112S = -1;
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42876b(StreamWriter streamWriter) throws IOException {
        super.mo42876b(streamWriter);
        String str = this.f12119z;
        if (str != null) {
            this.f12113t.put("fromaccount", str);
        } else {
            this.f12113t.remove("fromaccount");
        }
        long j = this.f12117x;
        if (j != 0) {
            this.f12113t.put("timesmart", new UInt(j).toString());
        }
        streamWriter.writeObjectList(null);
        streamWriter.writeStringMap(this.f12113t);
        streamWriter.writeStringPairList(this.f12114u);
        streamWriter.writeUInt32T(this.f12115v);
        streamWriter.writeUInt32T(this.f12116w);
        streamWriter.writeBoolean(this.f12118y);
        streamWriter.writeBoolean(false);
        this.f12113t.remove("fromaccount");
        this.f12113t.remove("version");
        this.f12113t.remove("spent");
        this.f12113t.remove("n");
        this.f12113t.remove("timesmart");
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42877b(SeriableData seriableData) throws IOException {
        super.mo42877b(seriableData);
        m10349c(null);
        VCMerkleTx.m10340a(this.wallet, seriableData, new Vector());
        seriableData.readStringMap(this.f12113t);
        seriableData.readStringPairList(this.f12114u);
        this.f12115v = seriableData.readUInt32();
        this.f12116w = seriableData.readUInt32();
        this.f12118y = seriableData.readBoolean();
        seriableData.readBoolean();
        this.f12119z = (String) this.f12113t.get("fromaccount");
        this.f12117x = this.f12113t.containsKey("timesmart") ? Long.parseLong((String) this.f12113t.get("timesmart")) : 0;
        this.f12113t.remove("fromaccount");
        this.f12113t.remove("version");
        this.f12113t.remove("spent");
        this.f12113t.remove("n");
        this.f12113t.remove("timesmart");
    }

    /* renamed from: ae */
    public void mo42882ae() {
        this.f12095B = false;
        this.f12097D = false;
        this.f12098E = false;
        this.f12099F = false;
        this.f12101H = false;
        this.f12100G = false;
        this.f12094A = false;
        this.f12102I = false;
    }

    /* renamed from: b */
    public void setSelfCWallet(CWallet mfVar) {
        super.setSelfCWallet(mfVar);
        mo42882ae();
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void writeSerialData(StreamWriter streamWriter) {
        super.writeSerialData(streamWriter);
        this.f12113t.put("fromaccount", this.f12119z);
        mo42881a(this.f12112S, this.f12113t);
        long j = this.f12117x;
        if (j != 0) {
            this.f12113t.put("timesmart", Long.toString(j));
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42881a(long j, HashMap<String, String> hashMap) {
        if (j != -1) {
            hashMap.put("n", Long.toString(j));
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void decodeSerialData(SeriableData seriableData) {
        super.decodeSerialData(seriableData);
        this.f12119z = (String) this.f12113t.get("fromaccount");
        this.f12112S = mo42880a(this.f12113t);
        this.f12117x = this.f12113t.containsKey("timesmart") ? Long.parseLong((String) this.f12113t.get("timesmart")) : 0;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public long mo42880a(HashMap<String, String> hashMap) {
        if (!hashMap.containsKey("n")) {
            return -1;
        }
        return Long.parseLong((String) hashMap.get("n"));
    }

    /* renamed from: E */
    public Transaction clone() {
        return new VCWalletTx((Transaction) this);
    }
}
