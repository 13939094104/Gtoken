package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.math.BigInteger;

//bme
public class PongMessage extends VMessage {

    /* renamed from: a */
    private BigInteger f11908a = BigInteger.valueOf(System.currentTimeMillis());

    public PongMessage(@NonNull Wallet izVar) {
        super(izVar, "pong");
    }

    public PongMessage(@NonNull Wallet izVar, BigInteger bigInteger) {
        super(izVar, "pong");
        this.f11908a = bigInteger;
    }

    public void onDecodeSerialData() {
        this.f11908a = readUInt64();
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeUInt64(this.f11908a);
    }
}
