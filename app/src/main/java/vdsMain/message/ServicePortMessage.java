package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.peer.Peer;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//bmj
public class ServicePortMessage extends VMessage {

    /* renamed from: a */
    private Peer f11921a;

    /* renamed from: b */
    private int f11922b;

    /* renamed from: h */
    private int f11923h;

    /* renamed from: i */
    private int f11924i;

    public ServicePortMessage(@NonNull Wallet izVar) {
        super(izVar, "serviceport");
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeUInt16(this.f11922b);
    }

    public void onDecodeSerialData() throws IOException {
        super.onDecodeSerialData();
        this.f11922b = readUInt16LE();
        try {
            this.f11923h = readUInt16LE();
            this.f11924i = readUInt16LE();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: a */
    public int mo42587a() {
        return this.f11922b;
    }

    /* renamed from: b */
    public Peer mo42589b() {
        return this.f11921a;
    }

    /* renamed from: a */
    public void mo42588a(Peer lhVar) {
        this.f11921a = lhVar;
    }

    /* renamed from: c */
    public int getSelfProtocalVersion() {
        return this.f11923h;
    }

    /* renamed from: d */
    public int mo42590d() {
        return this.f11924i;
    }
}
