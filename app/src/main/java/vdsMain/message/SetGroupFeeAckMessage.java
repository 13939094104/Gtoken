package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//boo
public class SetGroupFeeAckMessage extends ContractGroupAckMessage {
    public SetGroupFeeAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "setgfeeack", i);
    }
}
