package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bna
public class CancelDealAckMessage extends ContractGroupAckMessage {
    public CancelDealAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "cancelack", i);
    }
}
