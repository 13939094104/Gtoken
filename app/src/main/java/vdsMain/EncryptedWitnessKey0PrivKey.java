package vdsMain;

/* renamed from: w */
public class EncryptedWitnessKey0PrivKey extends EncryptedPrivateKey {
    public EncryptedWitnessKey0PrivKey() {
    }

    public EncryptedWitnessKey0PrivKey(byte[] bArr) {
        super(bArr);
    }

    /* renamed from: a */
    public EncryptedPrivateKey clone() {
        return new EncryptedWitnessKey0PrivKey(this.bytes);
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public CPrivateKeyInterface getNewCKey(byte[] bArr) {
        return new CWitnessPrivKey(bArr);
    }
}