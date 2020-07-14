package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.CTxDestination;
import vdsMain.wallet.Wallet;
import vdsMain.model.Address;
import java.util.List;

//bln
public class GetTx2Message extends GetTxMessage {
    public GetTx2Message(@NonNull Wallet izVar) {
        super(izVar);
        setTypeString("getx2");
    }

    public GetTx2Message(@NonNull Wallet izVar, long j, long j2, @NonNull List<Address> list, List<CTxDestination> list2) {
        super(izVar, j, j2, list, list2);
        setTypeString("getx2");
    }
}