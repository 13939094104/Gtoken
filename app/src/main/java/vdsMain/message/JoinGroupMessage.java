package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bnv
public class JoinGroupMessage extends ContractGroupMessage {
    public JoinGroupMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "psnaddgroup", i);
    }

    public JoinGroupMessage(@NonNull Wallet izVar, int i, String str) {
        super(izVar, "psnaddgroup", i, str);
    }
}
