package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bos
public class SetGroupNameAckMessage extends ContractGroupAckMessage {
    public SetGroupNameAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "setgnameack", i);
    }
}
