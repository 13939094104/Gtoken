package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bok
public class SetGroupAnnounceAckMessage extends ContractGroupAckMessage {
    public SetGroupAnnounceAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "setgannack", i);
    }
}
