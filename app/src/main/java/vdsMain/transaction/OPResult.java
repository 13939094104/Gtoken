package vdsMain.transaction;

public class OPResult {

    /* renamed from: a */
    //f12292a
    public ScriptChunk scriptChunk = null;

    /* renamed from: b */
    //f12293b
    public int opWord = 255;

    /* renamed from: c */
    //f12294c
    public byte[] bytes = null;

    /* renamed from: a */
    public int mo43150a() {
        byte[] bArr = this.bytes;
        if (bArr == null) {
            return 0;
        }
        return bArr.length;
    }
}