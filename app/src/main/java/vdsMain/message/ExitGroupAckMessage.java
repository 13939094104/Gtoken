package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bnn
public class ExitGroupAckMessage  extends ContractGroupAckMessage {
    public ExitGroupAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "psnexitgack", i);
    }
}
