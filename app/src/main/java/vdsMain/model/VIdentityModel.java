package vdsMain.model;


import androidx.annotation.NonNull;
import vdsMain.CTxDestination;
import vdsMain.table.AbstractTableItem;
import vdsMain.table.VidGroupTable;
import vdsMain.table.WalletTable;
import vdsMain.wallet.Wallet;

import java.util.HashMap;
import java.util.List;

//bpi
public class VIdentityModel extends Model {

    /* renamed from: a */
    private HashMap<CTxDestination, Address> f12019a = new HashMap<>();

    /* renamed from: b */
    private WalletTable f12020b;

    //f12021c
    private VidGroupTable vidGroupTable;

    /* renamed from: g */
    public void initAllDataFromDb() {
    }

    public VIdentityModel(@NonNull Wallet izVar) {
        super(izVar);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42684a(Address address) {
        if (address.mo44156B() && !address.isUnlessAddress()) {
            this.f12019a.put(address.getCTxDestination(), address);
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42689b(Address address) {
        this.f12019a.remove(address.getCTxDestination());
    }

    /* renamed from: a */
    public void mo42681a() {
        mo42688b();
    }

    /* renamed from: b */
    public void mo42688b() {
        this.f12019a.clear();
    }

    /* renamed from: c */
    public void mo42691c() {
        if (this.f12020b != null) {
            this.f12020b = this.wallet.getPersonalDB().getSelfAddressTable();
        }
        if (this.vidGroupTable == null) {
            this.vidGroupTable = this.wallet.getPersonalDB().getSelfVidGroupTable();
        }
    }

    //mo42692d
    public List<VidGroup> getVidGroupList() {
        VidGroupTable vidGroupTable = this.vidGroupTable;
        if (vidGroupTable == null) {
            return null;
        }
        return vidGroupTable.getVidGroupList();
    }

    /* renamed from: a */
    public void mo42685a(VidGroup jsVar) {
        this.vidGroupTable.mo44380b(jsVar);
    }

    /* renamed from: a */
    public void mo42682a(int i) {
        this.vidGroupTable.mo44381d(i);
    }

    /* renamed from: b */
    public void mo42690b(VidGroup jsVar) {
        this.vidGroupTable.replaceDataSynchronized((AbstractTableItem) jsVar);
    }

    /* renamed from: a */
    public void mo42683a(String str) {
        if (mo42687b(str) == null) {
            VidGroup jsVar = new VidGroup(this.vidGroupTable);
            jsVar.setGroupLabel(str);
            jsVar.setGroupIndex(this.vidGroupTable.mo44385j() + 1);
            this.vidGroupTable.mo44379a(jsVar);
        }
    }

    /* renamed from: b */
    public VidGroup mo42687b(String str) {
        return this.vidGroupTable.mo44383e(str);
    }

    /* renamed from: b */
    public VidGroup mo42686b(int i) {
        return this.vidGroupTable.mo44382e(i);
    }
}
