package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//boc
public class PayVulumnAndFeeAckMessage extends ContractGroupAckMessage {
    public PayVulumnAndFeeAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "pandfack", i);
    }
}

