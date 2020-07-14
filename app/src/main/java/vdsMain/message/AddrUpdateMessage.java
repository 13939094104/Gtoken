package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.CTxDestination;
import vdsMain.wallet.Wallet;

import java.util.Collection;

//bkp
public class AddrUpdateMessage extends AddrFilterMessage {
    public AddrUpdateMessage(@NonNull Wallet izVar) {
        super(izVar, "addru", null);
    }

    public AddrUpdateMessage(@NonNull Wallet izVar, Collection<CTxDestination> collection) {
        super(izVar, "addru", collection);
    }
}
