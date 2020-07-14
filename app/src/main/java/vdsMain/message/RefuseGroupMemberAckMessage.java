package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//boe
public class RefuseGroupMemberAckMessage extends ContractGroupAckMessage {
    public RefuseGroupMemberAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "refmemberack", i);
    }
}
