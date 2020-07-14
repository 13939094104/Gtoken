package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//boi
public class SetCurrencyAckMessage extends ContractGroupAckMessage {
    public SetCurrencyAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "setcack", i);
    }
}
