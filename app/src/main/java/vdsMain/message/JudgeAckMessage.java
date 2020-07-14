package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bnw
public class JudgeAckMessage extends ContractGroupAckMessage {
    public JudgeAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "judgeack", i);
    }
}
