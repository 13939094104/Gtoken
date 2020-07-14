package vdsMain.message;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import vdsMain.Log;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.Map;

//910 blr
public class GetBaseTxMessage extends VMessage {

    //f12011a
    private UInt256 blockHash;

    //f12012b
    private Map<UInt256, Object> txIdMap;

    public void onDecodeSerialData() {
    }

    public GetBaseTxMessage(@NonNull Wallet izVar) {
        super(izVar, "getbasetx");
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        this.blockHash.serialToStream(streamWriter);
        if (this.txIdMap != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("get base tx :");
            sb.append(this.txIdMap.size());
            Log.LogDebug("whw", sb.toString());
            streamWriter.writeVariableInt((long) this.txIdMap.size());
            for (Map.Entry key : this.txIdMap.entrySet()) {
                ((UInt256) key.getKey()).serialToStream(streamWriter);
            }
            return;
        }
        Log.LogDebug("whw", "get base tx : 0");
        streamWriter.writeVariableInt(0);
    }

    //mo42619a
    public void setBlockHash(UInt256 uInt256) {
        this.blockHash = uInt256;
    }

    //mo42620a
    public void setTxIdMap(Map<UInt256, Object> map) {
        this.txIdMap = map;
    }
}
