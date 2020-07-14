package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.CTxDestination;
import vdsMain.wallet.Wallet;

import java.util.List;

//bkl
public class AddrAddMessage extends AddrFilterMessage {
    public AddrAddMessage(@NonNull Wallet izVar) {
        super(izVar, "addra", null);
    }

    public AddrAddMessage(@NonNull Wallet izVar, List<CTxDestination> list) {
        super(izVar, "addra", list);
    }
}