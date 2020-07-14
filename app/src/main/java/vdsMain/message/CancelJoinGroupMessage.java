package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bnc
public class CancelJoinGroupMessage extends ContractGroupMessage {
    public CancelJoinGroupMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "psncancel", i);
    }
}
