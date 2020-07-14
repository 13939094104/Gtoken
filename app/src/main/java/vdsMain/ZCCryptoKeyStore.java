package vdsMain;


import androidx.annotation.NonNull;
import generic.crypto.KeyCryptor;

import java.util.HashMap;
import java.util.Map;
import vdsMain.model.Address;
import vdsMain.transaction.SaplingFullViewingKeyMap;
import vdsMain.transaction.SaplingIncomingViewingKeyMap;
import vdsMain.wallet.Wallet;

//brd
public class ZCCryptoKeyStore extends CCryptoKeyStore {

    private HashMap<SaplingFullViewingKey, Address> f12150d = new HashMap<>();

    //f12151e
    private SaplingFullViewingKeyMap saplingFullViewingKeyMap = new SaplingFullViewingKeyMap();


    //f12152f
    private SaplingIncomingViewingKeyMap saplingIncomingViewingKeyMap = new SaplingIncomingViewingKeyMap();

    public ZCCryptoKeyStore(@NonNull Wallet izVar, @NonNull KeyCryptor keyCryptor) {
        super(izVar, keyCryptor);
    }

    public boolean mo42930a(@NonNull Address jjVar) {
        synchronized (this.f398c) {
            SaplingPubKey bsg = (SaplingPubKey) jjVar.getSelfPubKey();
            SaplingFullViewingKey f = bsg.mo43025f();
            if (!mo42929a(f, bsg.mo43026i())) {
                return false;
            }
            this.f12150d.put(f, jjVar);
            return true;
        }
    }

    /* renamed from: b */
    public void mo42933b(Address jjVar) {
        synchronized (this.f398c) {
            SaplingPubKey bsg = (SaplingPubKey) jjVar.getSelfPubKey();
            SaplingFullViewingKey f = bsg.mo43025f();
            SaplingIncomingViewingKey a = f.mo42983a();
            this.saplingIncomingViewingKeyMap.remove(bsg.mo43026i());
            this.saplingFullViewingKeyMap.remove(a);
            this.f12150d.remove(f);
            for (Map.Entry entry :this.saplingIncomingViewingKeyMap.entrySet()) {
                if (((SaplingIncomingViewingKey) entry.getValue()).equals(a)) {
                    this.saplingIncomingViewingKeyMap.remove(entry.getKey());
                }
            }
        }
    }

    /* renamed from: a */
    public boolean mo42928a(SaplingFullViewingKey brx) {
        boolean containsKey;
        synchronized (this.f398c) {
            containsKey = this.f12150d.containsKey(brx);
        }
        return containsKey;
    }

    /* renamed from: b */
    public Address mo42932b(SaplingFullViewingKey brx) {
        return (Address) this.f12150d.get(brx);
    }

    /* renamed from: a */
    public boolean mo42929a(SaplingFullViewingKey brx, SaplingPaymentAddress bsf) {
        synchronized (this.f398c) {
            SaplingIncomingViewingKey a = brx.mo42983a();
            this.saplingFullViewingKeyMap.put(a, brx);
            mo42927a(a, bsf);
        }
        return true;
    }

    /* renamed from: a */
    public void mo42927a(SaplingIncomingViewingKey brz, SaplingPaymentAddress bsf) {
        synchronized (this.f398c) {
            this.saplingIncomingViewingKeyMap.put(bsf, brz);
        }
    }

    /* renamed from: c */
    public boolean mo42935c(SaplingFullViewingKey brx) {
        boolean z;
        synchronized (this.f398c) {
            Address jjVar = (Address) this.f12150d.get(brx);
            z = jjVar != null && jjVar.isRecycleFlag();
        }
        return z;
    }

    /* renamed from: a */
    public void clearNative() {
        super.clearNative();
        this.f12150d.clear();
        this.saplingFullViewingKeyMap.clear();
        this.saplingIncomingViewingKeyMap.clear();
    }

    //mo42931b
    public SaplingFullViewingKeyMap getSaplingFullViewingKeyMap() {
        return this.saplingFullViewingKeyMap;
    }

    /* renamed from: c */
    public SaplingIncomingViewingKeyMap mo42934c() {
        return this.saplingIncomingViewingKeyMap;
    }

}
