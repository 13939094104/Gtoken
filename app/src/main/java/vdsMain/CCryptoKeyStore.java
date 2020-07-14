package vdsMain;

import androidx.annotation.NonNull;
import generic.crypto.KeyCryptor;
import vdsMain.wallet.Wallet;

public abstract class CCryptoKeyStore extends CBasicKeyStore {
    public CCryptoKeyStore(@NonNull Wallet izVar, @NonNull KeyCryptor keyCryptor) {
        super(izVar, keyCryptor);
    }
}