package vdsMain;

import bitcoin.CKey;

public class BitcoinEncryptedPrivateKey extends EncryptedPrivateKey {
    public BitcoinEncryptedPrivateKey() {
    }

    public BitcoinEncryptedPrivateKey(EncryptedPrivateKey irVar) {
        super(irVar);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public CKey getNewCKey(byte[] bArr) {
        return new CKey(bArr);
    }

    /* renamed from: a */
    public EncryptedPrivateKey clone() {
        return new BitcoinEncryptedPrivateKey(this);
    }
}