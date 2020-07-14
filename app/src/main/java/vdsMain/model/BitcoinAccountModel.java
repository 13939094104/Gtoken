package vdsMain.model;

import androidx.annotation.NonNull;
import vdsMain.table.WalletTable;
import vdsMain.wallet.Wallet;

public class BitcoinAccountModel extends AccountModel {
    public BitcoinAccountModel(@NonNull Wallet izVar) {
        super(izVar);
    }

    //mo43125a
    public GeneralAccount getGeneralAccount(WalletTable walletTable) {
        return new BitcoinGeneralAccount(walletTable);
    }
}
