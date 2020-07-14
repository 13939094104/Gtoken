package vdsMain;

import java.util.HashMap;

/* renamed from: gp */
public class BytesKeyMap<T> {

    /* renamed from: a */
    private HashMap<ByteArrayObject, T> f12761a = new HashMap<>();

    /* renamed from: a */
    public void mo43675a(byte[] bArr, T t) {
        if (bArr != null) {
            this.f12761a.put(new ByteArrayObject(bArr), t);
            return;
        }
        throw new NullPointerException("Key is null");
    }

    /* renamed from: a */
    public void mo43674a() {
        this.f12761a.clear();
    }
}