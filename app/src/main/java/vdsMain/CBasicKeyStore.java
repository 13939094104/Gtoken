package vdsMain;

import androidx.annotation.NonNull;
import bitcoin.CKeyStore;
import generic.crypto.KeyCryptor;
import vdsMain.wallet.Wallet;

public class CBasicKeyStore extends CKeyStore {
    public CBasicKeyStore(@NonNull Wallet izVar, @NonNull KeyCryptor keyCryptor) {
        super(izVar, keyCryptor);
    }
}