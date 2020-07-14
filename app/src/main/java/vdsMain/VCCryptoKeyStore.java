package vdsMain;

import androidx.annotation.NonNull;
import generic.crypto.KeyCryptor;
import vdsMain.wallet.Wallet;

//bin
public class VCCryptoKeyStore extends ZCCryptoKeyStore {
    public VCCryptoKeyStore(@NonNull Wallet izVar, @NonNull KeyCryptor keyCryptor) {
        super(izVar, keyCryptor);
    }
}