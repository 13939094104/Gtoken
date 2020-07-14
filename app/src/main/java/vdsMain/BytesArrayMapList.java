package vdsMain;

import java.util.ArrayList;
import java.util.List;

public class BytesArrayMapList {

    /* renamed from: a */
    private BytesKeyMap<byte[]> f12759a = new BytesKeyMap<>();

    /* renamed from: b */
    private List<byte[]> f12760b = new ArrayList();

    /* renamed from: a */
    public void mo43669a(BytesArrayMapList goVar) {
        mo43673c();
        if (goVar != null) {
            for (byte[] c : goVar.f12760b) {
                byte[] c2 = DataTypeToolkit.bytesCopy(c);
                this.f12759a.mo43675a(c2, c2);
                this.f12760b.add(c2);
            }
        }
    }

    /* renamed from: a */
    public synchronized void mo43671a(byte[] bArr) {
        this.f12759a.mo43675a(bArr, bArr);
        this.f12760b.add(bArr);
    }

    /* renamed from: a */
    public List<byte[]> mo43668a() {
        return this.f12760b;
    }

    /* renamed from: b */
    public int mo43672b() {
        return this.f12760b.size();
    }

    /* renamed from: c */
    public synchronized void mo43673c() {
        this.f12759a.mo43674a();
        this.f12760b.clear();
    }

    /* renamed from: a */
    public synchronized void mo43670a(List<byte[]> list) {
        this.f12760b.addAll(list);
        for (byte[] bArr : list) {
            this.f12759a.mo43675a(bArr, bArr);
        }
    }
}
