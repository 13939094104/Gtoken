package vdsMain.wallet;

import androidx.annotation.NonNull;
import bitcoin.CKeyStore;
import bitcoin.UInt256;
import generic.crypto.KeyCryptor;
import vdsMain.CCryptoKeyStore;

import java.util.HashMap;

public abstract class CWallet {

    /* renamed from: a */
    public Object f13376a = new Object();

    /* renamed from: b */
    protected CCryptoKeyStore f13377b;

    /* renamed from: c */
    public HashMap<UInt256, Integer> f13378c = new HashMap<>();

    /* renamed from: d */
    protected Wallet f13379d;

    /* access modifiers changed from: protected */
    //mo42879a
    public abstract CCryptoKeyStore getCCroptoKeyStore(Wallet izVar, KeyCryptor keyCryptor);

    public CWallet(@NonNull Wallet izVar, @NonNull KeyCryptor keyCryptor) {
        this.f13379d = izVar;
        this.f13377b = getCCroptoKeyStore(izVar, keyCryptor);
    }

    //mo44714a
    public CKeyStore getCKeyStore() {
        return this.f13377b;
    }
}
