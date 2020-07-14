package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bof
public class RefuseGroupMemberMessage extends GroupMemberMessage {
    public RefuseGroupMemberMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "refmember", i);
    }

    public RefuseGroupMemberMessage(@NonNull Wallet izVar, int i, String str, String str2) {
        super(izVar, "refmember", i, str, str2);
    }
}
