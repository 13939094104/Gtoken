package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bmv
public class AddGroupMemberAckMessage extends ContractGroupAckMessage {
    public AddGroupMemberAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "addmemberack", i);
    }
}
