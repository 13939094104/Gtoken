package vdsMain.message;


import androidx.annotation.NonNull;
import vdsMain.CTxDestination;
import vdsMain.wallet.Wallet;

import java.util.List;

//bkm
public class AddrDeleteMessage extends AddrFilterMessage {
    public AddrDeleteMessage(@NonNull Wallet izVar) {
        super(izVar, "addrd", null);
    }

    public AddrDeleteMessage(@NonNull Wallet izVar, List<CTxDestination> list) {
        super(izVar, "addrd", list);
    }
}
