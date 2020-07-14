package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bom
public class SetGroupBaseInfoAckMessage extends ContractGroupAckMessage {
    public SetGroupBaseInfoAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "stbinfoack", i);
    }
}