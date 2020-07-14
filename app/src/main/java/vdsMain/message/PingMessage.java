package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.math.BigInteger;

//bmd
public class PingMessage extends VMessage {

    //f11907a
    private BigInteger curTimeMills = BigInteger.valueOf(System.currentTimeMillis());

    public PingMessage(@NonNull Wallet izVar) {
        super(izVar, "ping");
    }

    public void onDecodeSerialData() throws IOException {
        super.onDecodeSerialData();
        this.curTimeMills = readUInt64();
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeUInt64(this.curTimeMills);
    }

    //mo42582a
    public BigInteger getCurTimeMills() {
        return this.curTimeMills;
    }
}
