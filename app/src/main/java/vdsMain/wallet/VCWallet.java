package vdsMain.wallet;

import androidx.annotation.NonNull;
import generic.crypto.KeyCryptor;
import vdsMain.CCryptoKeyStore;
import vdsMain.VCCryptoKeyStore;


//bqp
public class VCWallet extends CWallet {
    public VCWallet(@NonNull Wallet izVar, @NonNull KeyCryptor keyCryptor) {
        super(izVar, keyCryptor);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public CCryptoKeyStore getCCroptoKeyStore(Wallet izVar, KeyCryptor keyCryptor) {
        return new VCCryptoKeyStore(this.f13379d, keyCryptor);
    }
}