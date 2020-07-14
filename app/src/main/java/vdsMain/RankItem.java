package vdsMain;

import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.UnsupportedEncodingException;

//bqk
public class RankItem extends SeriableData {

    /* renamed from: a */
    private int f12082a;

    /* renamed from: b */
    private String f12083b;

    /* renamed from: c */
    private double f12084c;

    public void writeSerialData(StreamWriter streamWriter) {
    }

    public RankItem() {
    }

    public RankItem(int i, String str, double d) {
        this.f12082a = i;
        this.f12083b = str;
        this.f12084c = d;
    }

    /* renamed from: a */
    public int mo42844a() {
        return this.f12082a;
    }

    /* renamed from: b */
    public String mo42846b() {
        return this.f12083b;
    }

    /* renamed from: a */
    public void mo42845a(String str) {
        this.f12083b = str;
    }

    /* renamed from: c */
    public double mo42847c() {
        return this.f12084c;
    }

    public void onDecodeSerialData() throws UnsupportedEncodingException {
        this.f12082a = readInt32();
        this.f12083b = readVariableString();
        this.f12084c = readDouble();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof RankItem)) {
            return false;
        }
        RankItem bqk = (RankItem) obj;
        if (this.f12082a != bqk.f12082a || !this.f12083b.equals(bqk.mo42846b()) || Math.abs(this.f12084c - bqk.f12084c) >= 1.0E-8d) {
            return false;
        }
        return true;
    }
}
