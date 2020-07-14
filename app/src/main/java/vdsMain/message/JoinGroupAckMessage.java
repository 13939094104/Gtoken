package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bnu
public class JoinGroupAckMessage extends ContractGroupAckMessage {
    public JoinGroupAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "psnaddgrack", i);
    }
}
