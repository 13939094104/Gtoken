package vdsMain;

import bitcoin.script.CScript;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.UnsupportedEncodingException;

//bqj
public class QueneItem extends SeriableData {

    /* renamed from: a */
    public String f12078a;

    /* renamed from: b */
    public String f12079b;

    /* renamed from: c */
    public CScript f12080c;

    /* renamed from: d */
    public TagParaTxMonitor f12081d;

    public void writeSerialData(StreamWriter streamWriter) {
    }

    public void onDecodeSerialData() throws UnsupportedEncodingException {
        this.f12078a = readVariableString();
        this.f12079b = readVariableString();
        this.f12080c = new CScript();
        this.f12080c.decodeSerialStream(getTempInput());
        this.f12081d = new TagParaTxMonitor();
        this.f12081d.decodeSerialStream(getTempInput());
    }
}
