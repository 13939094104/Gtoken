package vdsMain;


import com.vc.libcommon.exception.AddressFormatException;

import java.io.IOException;
import java.util.Locale;

//brt
public class SaplingEncryptedKey extends EncryptedPrivateKey {
    public SaplingEncryptedKey() {
    }

    public SaplingEncryptedKey(EncryptedPrivateKey irVar) {
        super(irVar);
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public CPrivateKeyInterface getNewCKey(byte[] bArr) throws AddressFormatException {
        if (bArr.length == 169) {
            try {
                return new SaplingExtendedSpendingKey(bArr);
            } catch (IOException e) {
                throw new AddressFormatException((Throwable) e);
            }
        } else {
            throw new AddressFormatException(String.format(Locale.getDefault(), "Invalidate sapling private key length: %d", new Object[]{Integer.valueOf(bArr.length)}));
        }
    }

    /* renamed from: a */
    public EncryptedPrivateKey clone() {
        return new SaplingEncryptedKey(this);
    }
}