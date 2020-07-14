package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bmx
public class ApproveAckMessage extends ContractGroupAckMessage {
    public ApproveAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "approveack", i);
    }
}
