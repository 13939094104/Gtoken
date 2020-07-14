package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bod
public class PayVulumnAndFeeMessage extends ContractGroupMessage {
    public PayVulumnAndFeeMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "pandf", i);
    }

    public PayVulumnAndFeeMessage(@NonNull Wallet izVar, int i, String str) {
        super(izVar, "pandf", i, str);
    }
}
