package vdsMain;

import bitcoin.CPubKey;
import bitcoin.UInt256;
import bitcoin.script.CScript;
import com.vc.libcommon.exception.AddressFormatException;
import net.bither.bitherj.exception.ScriptException;
import vdsMain.transaction.CScriptID;
import vdsMain.transaction.Script;
import vdsMain.transaction.TxnOutType;

public class CMultisigPubkey extends CPubKey {

    /* renamed from: a */
    protected int f13403a;

    public boolean RecoverCompact(byte[] bArr, byte[] bArr2) {
        return true;
    }

    public boolean Verify(byte[] bArr, byte[] bArr2, byte[] bArr3, int i) {
        return true;
    }

    /* renamed from: a */
    public int getBytesRequireLength() {
        return 0;
    }

    /* renamed from: a */
    public boolean verfiySignture(UInt256 uInt256, byte[] bArr) {
        return true;
    }

    /* renamed from: b */
    public boolean mo9441b(UInt256 uInt256, byte[] bArr) {
        return true;
    }

    /* renamed from: e */
    public boolean checkLength() {
        return false;
    }

    public CMultisigPubkey() {
    }

    public CMultisigPubkey(byte[] bArr) throws AddressFormatException {
        super(bArr);
    }

    public CMultisigPubkey(CPubKey cPubKey) throws AddressFormatException {
        mo9437a(cPubKey);
    }

    /* renamed from: a */
    public int mo9435a(byte b) {
        return mo9436a(this.byteArr);
    }

    /* renamed from: a */
    public int mo9436a(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return 0;
        }
        return bArr.length;
    }

    public void Set(byte[] bArr) throws AddressFormatException {
        if (bArr == null || bArr.length == 0) {
            init();
            return;
        }
        super.Set(bArr);
        this.f13403a = 0;
        try {
            Script script = new Script(bArr);
            this.f13403a = script.mo43174o();
            if (script.getTxnOutType() != TxnOutType.TX_MULTISIG) {
                throw new AddressFormatException("Data must be multisig script pubkey!");
            } else if (script.mo43174o() < 1) {
                throw new AddressFormatException("Data must be multisig script pubkey!");
            } else if (!mo9444d()) {
                throw new AddressFormatException("Invalidate multisig pubkey.");
            }
        } catch (ScriptException e) {
            throw new AddressFormatException((Throwable) e);
        }
    }

    /* renamed from: b */
    public void init() {
        this.byteArr = null;
        this.f401c = 0;
        this.f13403a = 0;
    }

    /* renamed from: c */
    public int getTypeLength() {
        if (this.byteArr == null) {
            return 0;
        }
        return this.byteArr.length;
    }

    /* renamed from: d */
    public boolean mo9444d() {
        return m13372b(this.byteArr);
    }

    /* renamed from: a */
    public void mo9437a(CPubKey cPubKey) throws AddressFormatException {
        this.f13403a = 0;
        if (cPubKey == null) {
            init();
        } else {
            Set(cPubKey.getByteArr());
        }
    }

    /* renamed from: f */
    public boolean mo9447f() {
        return m13372b(this.byteArr);
    }

    /* renamed from: b */
    public static boolean m13372b(byte[] bArr) {
        if (bArr == null) {
            return false;
        }
        try {
            Script crVar = new Script(bArr);
            if (crVar.getTxnOutType() == TxnOutType.TX_MULTISIG && crVar.mo43174o() >= 1) {
                return true;
            }
            return false;
        } catch (ScriptException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String toString() {
        return super.toString();
    }

    /* renamed from: g */
    public PubkeyType mo210g() {
        return PubkeyType.MULTISIG;
    }

    /* renamed from: h */
    public CTxDestination getCKeyID() {
        if (this.byteArr == null || this.byteArr.length == 0) {
            return new CScriptID();
        }
        return new CScriptID(CHash160.encodeToUInt160(this.byteArr));
    }

    /* renamed from: i */
    public CScript mo9450i() {
        if (this.byteArr == null || this.byteArr.length == 0) {
            return new CScript();
        }
        return CScript.m484a(getCKeyID());
    }

    /* renamed from: j */
    public CScript mo9451j() {
        return CScript.m486b((CPubKey) this);
    }

    /* renamed from: k */
    public CPubKey clone() {
        try {
            return new CMultisigPubkey((CPubKey) this);
        } catch (AddressFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}
