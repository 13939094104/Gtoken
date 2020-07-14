package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bnb
public class CancelDealMessage extends ContractGroupMessage {
    public CancelDealMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "cancel", i);
    }

    public CancelDealMessage(@NonNull Wallet izVar, int i, String str) {
        super(izVar, "cancel", i, str);
    }
}
