package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bou
public class SetGroupRuleAckMessage extends ContractGroupAckMessage {
    public SetGroupRuleAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "setgruleack", i);
    }
}
