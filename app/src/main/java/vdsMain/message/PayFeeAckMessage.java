package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//boa
public class PayFeeAckMessage extends ContractGroupAckMessage {
    public PayFeeAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "payfeeack", i);
    }
}
