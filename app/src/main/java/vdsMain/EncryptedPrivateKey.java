package vdsMain;

import com.vc.libcommon.exception.AddressFormatException;
import generic.crypto.Base58;
import generic.crypto.KeyCryptor;

import java.util.Arrays;

public abstract class EncryptedPrivateKey {

    //f12962a
    protected byte[] bytes;

    /* renamed from: a */
    public abstract EncryptedPrivateKey clone();

    /* access modifiers changed from: protected */
    //mo60b
    public abstract CPrivateKeyInterface getNewCKey(byte[] bArr) throws AddressFormatException;

    public EncryptedPrivateKey() {
    }

    public EncryptedPrivateKey(EncryptedPrivateKey irVar) {
        this.bytes = DataTypeToolkit.bytesCopy(irVar.bytes);
    }

    public EncryptedPrivateKey(byte[] bArr) {
        initFromOtherBytes(bArr);
    }

    //mo44008c
    public void initFromOtherBytes(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            this.bytes = null;
            return;
        }
        this.bytes = new byte[bArr.length];
        System.arraycopy(bArr, 0, this.bytes, 0, bArr.length);
    }

    //mo44007b
    public byte[] getBytes() {
        return this.bytes;
    }

    //mo44006a
    public void encryptBytesByPwd(byte[] bArr, KeyCryptor keyCryptor, String str) {
        this.bytes = keyCryptor.encrypt(bArr, str);
    }

    //mo44004a
    public CPrivateKeyInterface getNewCKeyWithKeyCryptor(KeyCryptor keyCryptor, String str) throws AddressFormatException {
        return getNewCKey(keyCryptor.decrypt(this.bytes, str));
    }

    //mo44005a
    //915 mo44349a
    public void updateBytesByNewPwd(KeyCryptor keyCryptor, String str, String str2) {
        this.bytes = keyCryptor.encrypt(keyCryptor.decrypt(this.bytes, str), str2);
    }

    public String toString() {
        byte[] bArr = this.bytes;
        if (bArr == null) {
            return "";
        }
        return Base58.encodeToString(bArr);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof EncryptedPrivateKey)) {
            return false;
        }
        return Arrays.equals(this.bytes, ((EncryptedPrivateKey) obj).bytes);
    }

    //mo44009c
    public boolean isBytesEmpty() {
        byte[] bArr = this.bytes;
        return bArr == null || bArr.length == 0;
    }
}
