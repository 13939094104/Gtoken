package vdsMain.transaction;

import bitcoin.CKey;
import bitcoin.script.BaseSignatureChecker;
import bitcoin.script.CScript;
import bitcoin.script.Interpreter;
import bitcoin.script.MutableTransactionSignatureChecker;
import vdsMain.BytesArrayBuffer;
import vdsMain.CKeyID;
import vdsMain.CPrivateKeyInterface;
import vdsMain.CTxDestination;

import java.io.IOException;

public class MutableTransactionSignatureCreator implements BaseSignatureCreator {

    /* renamed from: a */
    protected CMutableTransaction f12287a;

    /* renamed from: b */
    protected int f12288b;

    /* renamed from: c */
    protected int f12289c;

    /* renamed from: d */
    protected long f12290d;

    /* renamed from: e */
    protected MutableTransactionSignatureChecker f12291e;

    public MutableTransactionSignatureCreator(CMutableTransaction dfVar, int i, long j, int... iArr) {
        this.f12287a = dfVar;
        this.f12288b = i;
        if (iArr.length > 0) {
            this.f12289c = iArr[0];
        } else {
            this.f12289c = 1;
        }
        this.f12290d = j;
        this.f12291e = new MutableTransactionSignatureChecker(this.f12287a, this.f12288b, j);
    }

    /* renamed from: a */
    public BaseSignatureChecker mo43139a() {
        return this.f12291e;
    }

    /* renamed from: a */
    public boolean mo43140a(SigningProvider daVar, CharSequence charSequence, BytesArrayBuffer gdVar, CKeyID lVar, CScript cScript, SigVersion cxVar) {
        CPrivateKeyInterface a = daVar.mo42761a((CTxDestination) lVar, charSequence);
        if (a == null) {
            return false;
        }
        if (cxVar == SigVersion.WITNESS_V0 && !a.IsCompressed()) {
            return false;
        }
        try {
            byte[] a2 = ((CKey) a).signNativeByTransactionData(new Interpreter().mo9560a(this.f12287a, cScript, this.f12288b, this.f12289c, this.f12290d, cxVar, null), true, 0);
            if (a2 == null) {
                return false;
            }
            gdVar.writeAllBytes(a2);
            gdVar.writeOnByte((byte) this.f12289c);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}