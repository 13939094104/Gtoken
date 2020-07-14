package vdsMain;

import io.reactivex.annotations.NonNull;
import vdsMain.model.AddressModel;
import vdsMain.transaction.CScriptID;
import vdsMain.wallet.Wallet;
import vdsMain.model.Address;
import java.util.*;

//bja
public class ComplexBitcoinAddress {

    //f11716a
    protected CScriptID cScriptID;

    /* renamed from: b */
    protected LinkedHashMap<CTxDestination, Boolean> f11717b = new LinkedHashMap<>();

    //f11718c
    private Wallet wallet;

    public ComplexBitcoinAddress(@NonNull Wallet izVar, @NonNull CScriptID ckVar, List<CTxDestination>... listArr) {
        this.wallet = izVar;
        this.cScriptID = ckVar;
        if (listArr.length > 0) {
            this.f11717b = new LinkedHashMap<>(2);
            for (CTxDestination put : listArr[0]) {
                this.f11717b.put(put, Boolean.valueOf(true));
            }
        }
    }

    /* renamed from: a */
    public CScriptID mo42488a() {
        return this.cScriptID;
    }

    /* renamed from: b */
    public synchronized Set<CTxDestination> mo42491b() {
        return this.f11717b.keySet();
    }

    /* renamed from: c */
    public synchronized List<Address> mo42493c() {
        if (this.f11717b != null) {
            if (!this.f11717b.isEmpty()) {
                ArrayList arrayList = new ArrayList(this.f11717b.size());
                for (Map.Entry key : this.f11717b.entrySet()) {
                    Address b = this.wallet.getAddressByCTxDestinationFromArrayMap((CTxDestination) key.getKey());
                    if (b != null) {
                        arrayList.add(b);
                    }
                }
                return arrayList;
            }
        }
        return null;
    }

    /* renamed from: d */
    public synchronized HashSet<Address> mo42494d() {
        HashSet<Address> hashSet;
        hashSet = new HashSet<>(2);
        Address address = this.wallet.getAddressByCTxDestinationFromArrayMap((CTxDestination) this.cScriptID);
        AddressModel addressModel = this.wallet.getSelfAddressModel();
        if (address != null) {
            hashSet.add(address);
        } else {
            Address b2 = addressModel.getAddressFromUsingAddressShadowMap((CTxDestination) this.cScriptID);
            if (b2 != null) {
                hashSet.add(b2);
            }
        }
        if (this.f11717b != null && !this.f11717b.isEmpty()) {
            for (Map.Entry entry : this.f11717b.entrySet()) {
                Address b3 = this.wallet.getAddressByCTxDestinationFromArrayMap((CTxDestination) entry.getKey());
                if (b3 != null) {
                    hashSet.add(b3);
                } else {
                    Address b4 = addressModel.getAddressFromUsingAddressShadowMap((CTxDestination) entry.getKey());
                    if (b4 != null) {
                        hashSet.add(b4);
                    }
                }
            }
        }
        return hashSet;
    }

    /* renamed from: a */
    public synchronized void mo42490a(CTxDestination oVar) {
        if (this.f11717b == null) {
            this.f11717b = new LinkedHashMap<>(2);
            this.f11717b.put(oVar, Boolean.valueOf(true));
        } else if (!this.f11717b.containsKey(oVar)) {
            this.f11717b.put(oVar, Boolean.valueOf(true));
        }
    }

    /* renamed from: a */
    public synchronized void mo42489a(List<CTxDestination> list) {
        if (this.f11717b == null) {
            this.f11717b = new LinkedHashMap<>(2);
        }
        for (CTxDestination oVar : list) {
            if (!this.f11717b.containsKey(oVar)) {
                this.f11717b.put(oVar, Boolean.valueOf(true));
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0038, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003a, code lost:
        return;
     */
    /* renamed from: b */
    public synchronized void mo42492b(List<CTxDestination> list) {
        if (!(this.f11717b == null || this.f11717b.isEmpty() || list == null)) {
            if (!list.isEmpty()) {
                for (CTxDestination remove : list) {
                    this.f11717b.remove(remove);
                }
                if (this.f11717b.isEmpty()) {
                    this.f11717b = null;
                }
            }
        }
    }

    /* renamed from: e */
    public boolean mo42495e() {
        LinkedHashMap<CTxDestination, Boolean> linkedHashMap = this.f11717b;
        return linkedHashMap != null && !linkedHashMap.isEmpty();
    }
}
