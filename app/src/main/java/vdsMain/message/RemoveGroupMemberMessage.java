package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//boh
public class RemoveGroupMemberMessage extends GroupMemberMessage {
    public RemoveGroupMemberMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "rmvmember", i);
    }

    public RemoveGroupMemberMessage(@NonNull Wallet izVar, int i, String str, String str2) {
        super(izVar, "rmvmember", i, str, str2);
    }
}
