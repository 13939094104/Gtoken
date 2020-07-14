package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//boq
public class SetGroupFeeOtcPercentAckMessage extends ContractGroupAckMessage {
    public SetGroupFeeOtcPercentAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "setgfotcpack", i);
    }
}
