package vdsMain.model;

import androidx.annotation.NonNull;
import vdsMain.table.WalletTable;

public class BitCoinVAddress extends BitCoinAddress {
    public BitCoinVAddress(@NonNull WalletTable kjVar) {
        super(kjVar);
        setFlag(16, true);
    }
}