package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bow
public class SetOtcPercentAckMessage extends ContractGroupAckMessage {
    public SetOtcPercentAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "setotcpack", i);
    }
}
