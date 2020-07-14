package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//ble
public class GetBlocksMessage extends GetHeadersMessage {
    public GetBlocksMessage(@NonNull Wallet izVar) {
        super(izVar);
        setTypeString("getblocks");
    }
}