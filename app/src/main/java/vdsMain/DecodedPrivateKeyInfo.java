package vdsMain;

import bitcoin.CKey;
import generic.crypto.Base58;

import java.io.IOException;

public class DecodedPrivateKeyInfo {

    /* renamed from: a */
    private byte[] f13094a;

    /* renamed from: b */
    private byte[] f13095b;

    /* renamed from: c */
    private byte[] f13096c;

    //f13097d
    private HeaderType headerType = HeaderType.UNKNOWN;

    public DecodedPrivateKeyInfo() {
    }

    public DecodedPrivateKeyInfo(HeaderType jnVar, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        this.f13094a = bArr;
        this.f13095b = bArr2;
        this.headerType = jnVar;
        this.f13096c = bArr3;
    }

    //mo44258a
    public CPrivateKeyInterface getPrivateKey() {
        switch (this.headerType) {
            case PRIVATE_KEY:
                return new CKey(this.f13095b, this.f13096c.length == this.f13094a.length + 33);
            case PRIVATE_KEY_ANONYMOUS:
                try {
                    return new SaplingExtendedSpendingKey(this.f13095b);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
        }
        return null;
    }

    public String toString() {
        int length = this.f13094a.length;
        byte[] bArr = this.f13095b;
        int length2 = length + bArr.length;
        if (bArr.length == 32) {
            length2++;
        }
        byte[] bArr2 = new byte[length2];
        byte[] bArr3 = this.f13094a;
        System.arraycopy(bArr3, 0, bArr2, 0, bArr3.length);
        byte[] bArr4 = this.f13095b;
        System.arraycopy(bArr4, 0, bArr2, this.f13094a.length, bArr4.length);
        if (this.f13095b.length == 32) {
            bArr2[length2 - 1] = 1;
        }
        if (bArr2.length == 0) {
            return "null";
        }
        return Base58.encodeChecked(bArr2).toString();
    }
}