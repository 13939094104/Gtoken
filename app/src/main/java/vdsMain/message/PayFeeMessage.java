package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bob
public class PayFeeMessage extends ContractGroupMessage {
    public PayFeeMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "payfee", i);
    }

    public PayFeeMessage(@NonNull Wallet izVar, int i, String str) {
        super(izVar, "payfee", i, str);
    }
}
