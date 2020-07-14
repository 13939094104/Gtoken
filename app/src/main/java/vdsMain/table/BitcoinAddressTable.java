package vdsMain.table;

import androidx.annotation.NonNull;
import vdsMain.db.WalletDB;

public class BitcoinAddressTable extends AddressTable{
    public BitcoinAddressTable(@NonNull WalletDB kiVar) {
        super(kiVar);
    }
}
