package vdsMain.message;

import bitcoin.CPubKey;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import io.reactivex.annotations.NonNull;
import vdsMain.*;
import vdsMain.tool.Hash;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

//bkq
public class AlertMessage extends VMessage {

    /* renamed from: a */
    private int f11838a = 0;

    /* renamed from: b */
    private long f11839b = 0;

    /* renamed from: h */
    private long f11840h = 0;

    /* renamed from: i */
    private int f11841i = 0;

    /* renamed from: m */
    private int f11842m = 0;

    /* renamed from: n */
    private Vector<Integer> f11843n = new Vector<>();

    /* renamed from: o */
    private int f11844o = 0;

    /* renamed from: p */
    private int f11845p = 0;

    /* renamed from: q */
    private Vector<String> f11846q = new Vector<>();

    /* renamed from: r */
    private int f11847r = 0;

    /* renamed from: s */
    private String f11848s = null;

    /* renamed from: t */
    private String f11849t = null;

    /* renamed from: u */
    private String f11850u = null;

    /* renamed from: v */
    private byte[] f11851v = null;

    /* renamed from: w */
    private byte[] f11852w = null;

    public AlertMessage(@NonNull Wallet izVar) {
        super(izVar, "alert");
    }

    public void onDecodeSerialData() throws IOException {
        super.onDecodeSerialData();
        this.f11852w = readVariableBytes();
        this.f11851v = readVariableBytes();
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeVariableBytes(this.f11852w);
        streamWriter.writeVariableBytes(this.f11851v);
    }

    /* renamed from: a */
    public boolean mo42553a(VChainParam param) {
        try {
            if (!new CPubKey(param.f11675af).verfiySignture(Hash.m10362a(this.f11852w), this.f11851v)) {
                Log.infoObject((Object) this, "CAlert::CheckSignature(): verify signature failed");
                return false;
            }
            m9814a((SeriableData) new DummySeriableData(new ByteBuffer(this.f11852w, 0)));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* renamed from: a */
    public boolean mo42552a() {
        return TimeData.m122b() < this.f11840h;
    }

    /* renamed from: b */
    public boolean mo42554b(VChainParam biq) {
        if (mo42553a(biq) && mo42552a()) {
            return true;
        }
        return false;
    }

    /* renamed from: a */
    private void m9814a(SeriableData seriableData) throws UnsupportedEncodingException {
        this.f11838a = seriableData.readInt32();
        this.f11839b = seriableData.readInt64();
        this.f11840h = seriableData.readInt64();
        this.f11841i = seriableData.readInt32();
        this.f11842m = seriableData.readInt32();
        this.f11843n.clear();
        int b = seriableData.readVariableInt().getIntValue();
        for (int i = 0; i < b; i++) {
            this.f11843n.add(Integer.valueOf(seriableData.readInt32()));
        }
        this.f11844o = seriableData.readInt32();
        this.f11845p = seriableData.readInt32();
        this.f11846q.clear();
        int b2 = seriableData.readVariableInt().getIntValue();
        for (int i2 = 0; i2 < b2; i2++) {
            this.f11846q.add(seriableData.readVariableString());
        }
        this.f11847r = seriableData.readInt32();
        this.f11848s = seriableData.readVariableString();
        this.f11849t = seriableData.readVariableString();
        this.f11850u = seriableData.readVariableString();
    }
}

