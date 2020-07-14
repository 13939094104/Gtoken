package vdsMain.db;

import androidx.annotation.NonNull;
import vdsMain.table.AddressTable;
import vdsMain.table.BitcoinAddressTable;
import vdsMain.table.HDAccountTable;
import vdsMain.wallet.Wallet;

public class VPersonalDB extends PersonalDB {

    public VPersonalDB(@NonNull Wallet wallet, @NonNull String prefix, @NonNull String name, int i) {
        super(wallet, prefix, name, i);
    }

    public void InitAndAddtoDbVector() {
        super.InitAndAddtoDbVector();
//        this.f11825g = new bkd(this);
//        this.f11826h = new bke(this);
//        addAbstractTableByName(this.f11825g, "contacts");
//        addAbstractTableByName(this.f11826h, "groupContacts");
    }

    public AddressTable getAddressTable() {
        return new BitcoinAddressTable(this);
    }

    public HDAccountTable getHDAccountTable() {
        return new HDAccountTable(this);
    }

}
