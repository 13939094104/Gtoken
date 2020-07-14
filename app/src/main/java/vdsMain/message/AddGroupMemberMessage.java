package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bmw
public class AddGroupMemberMessage extends GroupMemberMessage {
    public AddGroupMemberMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "addmember", i);
    }

    public AddGroupMemberMessage(@NonNull Wallet izVar, int i, String str, String str2) {
        super(izVar, "addmember", i, str, str2);
    }
}
