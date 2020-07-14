package vdsMain;

public class EncryptedWitnessKey0ScriptPrviKey extends EncryptedPrivateKey {
    public EncryptedWitnessKey0ScriptPrviKey() {
    }

    public EncryptedWitnessKey0ScriptPrviKey(byte[] bArr) {
        super(bArr);
    }

    /* renamed from: a */
    public EncryptedPrivateKey clone() {
        return new EncryptedWitnessKey0ScriptPrviKey(this.bytes);
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public CPrivateKeyInterface getNewCKey(byte[] bArr) {
        return new WitnessScriptPrivKey(bArr);
    }
}
