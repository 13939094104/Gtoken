package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bog
public class RemoveGroupMemberAckMessage extends ContractGroupAckMessage {
    public RemoveGroupMemberAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "rmvmemberack", i);
    }
}
