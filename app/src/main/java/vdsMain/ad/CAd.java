package vdsMain.ad;

import bitcoin.UInt256;
import generic.keyid.CTxDestinationFactory;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.CTxDestination;

import java.io.IOException;

//biy
public class CAd extends SeriableData {

    /* renamed from: a */
    private UInt256 f11708a;

    /* renamed from: b */
    private int f11709b;

    /* renamed from: c */
    private CTxDestination f11710c;

    /* renamed from: d */
    private String f11711d;

    /* renamed from: e */
    private long f11712e;

    public CAd() {
    }

    public CAd(String str, int i, CTxDestination oVar, String str2, long j) {
        this.f11708a = UInt256.fromHex(str);
        this.f11709b = i;
        this.f11710c = oVar;
        this.f11711d = str2;
        this.f11712e = j;
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        if (this.f11708a == null) {
            this.f11708a = new UInt256();
        }
        this.f11708a.serialToStream(streamWriter);
        streamWriter.writeUInt32T((long) this.f11709b);
        this.f11710c.writeTypeAndData(streamWriter);
        streamWriter.writeVariableString(this.f11711d);
        streamWriter.writeUInt64(this.f11712e);
    }

    public void onDecodeSerialData() throws IOException {
        if (this.f11708a == null) {
            this.f11708a = new UInt256();
        }
        this.f11708a.decodeSerialStream(getTempInput());
        this.f11709b = readInt32();
        this.f11710c = CTxDestinationFactory.m910a((SeriableData) this);
        this.f11711d = readVariableString();
        this.f11712e = readInt64();
    }

    /* renamed from: a */
    public UInt256 mo42480a() {
        return this.f11708a;
    }

    /* renamed from: b */
    public int mo42481b() {
        return this.f11709b;
    }

    /* renamed from: c */
    public CTxDestination mo42482c() {
        return this.f11710c;
    }

    /* renamed from: d */
    public String mo42483d() {
        return this.f11711d;
    }

    /* renamed from: e */
    public long mo42484e() {
        return this.f11712e;
    }
}
