package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bpa
public class SetStartOtcAckMessage extends ContractGroupAckMessage {
    public SetStartOtcAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "setotcack", i);
    }
}
