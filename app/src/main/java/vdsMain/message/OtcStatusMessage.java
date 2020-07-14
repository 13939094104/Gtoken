package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bnz
public class OtcStatusMessage extends ContractGroupMessage {
    public OtcStatusMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "otcstatus", i);
    }
}
