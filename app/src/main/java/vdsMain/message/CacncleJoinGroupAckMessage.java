package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bmz
public class CacncleJoinGroupAckMessage extends ContractGroupAckMessage {
    public CacncleJoinGroupAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "psncancelack", i);
    }
}
