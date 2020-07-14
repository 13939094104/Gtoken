package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bmy
public class ApproveMessage extends ContractGroupMessage {
    public ApproveMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "approve", i);
    }

    public ApproveMessage(@NonNull Wallet izVar, int i, String str) {
        super(izVar, "approve", i, str);
    }
}
