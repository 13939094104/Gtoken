package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bnq
public class IncreaseGroupMaxMemberAckMessage extends ContractGroupAckMessage {
    public IncreaseGroupMaxMemberAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "incmaxack", i);
    }
}
