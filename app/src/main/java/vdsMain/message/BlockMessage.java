package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.block.BlockInfo;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//bks
public class BlockMessage extends VMessage {

    //f11855a
    private BlockInfo blockInfo = null;

    public BlockMessage(@NonNull Wallet izVar) {
        super(izVar, "block");
    }

    public void onDecodeSerialData() throws IOException {
        super.onDecodeSerialData();
        this.blockInfo = this.wallet.getSelfWalletHelper().getNewBlockInfo();
        this.blockInfo.decodeSerialItem(getTempInput());
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) {
        BlockInfo juVar = this.blockInfo;
        if (juVar != null) {
            juVar.mo44659c(streamWriter);
        }
    }
}
