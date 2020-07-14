package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//box
public class SetOtcPercentCurrencyAckMessage extends ContractGroupAckMessage {
    public SetOtcPercentCurrencyAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "setotccack", i);
    }
}
