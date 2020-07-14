package vdsMain;

import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.util.ArrayList;
import java.util.List;

//bql
public class Season extends SeriableData {

    /* renamed from: a */
    private int f12085a;

    /* renamed from: b */
    private long f12086b;

    /* renamed from: c */
    private List<RankItem> f12087c;

    public void writeSerialData(StreamWriter streamWriter) {
    }

    /* renamed from: a */
    public void mo42851a(long j) {
        this.f12086b = j;
    }

    /* renamed from: a */
    public int mo42849a() {
        return this.f12085a;
    }

    /* renamed from: a */
    public void mo42850a(int i) {
        this.f12085a = i;
    }

    /* renamed from: b */
    public List<RankItem> mo42853b() {
        return this.f12087c;
    }

    /* renamed from: a */
    public void mo42852a(List<RankItem> list) {
        this.f12087c = list;
    }

    /* renamed from: c */
    public List<String> mo42854c() {
        ArrayList arrayList = new ArrayList();
        List<RankItem> list = this.f12087c;
        if (list != null && !list.isEmpty()) {
            for (int size = this.f12087c.size() - 1; size >= 0; size--) {
                if (((RankItem) this.f12087c.get(size)).mo42844a() != 0) {
                    arrayList.add(((RankItem) this.f12087c.get(size)).mo42846b());
                }
            }
        }
        return arrayList;
    }

    /* renamed from: d */
    public List<String> mo42855d() {
        ArrayList arrayList = new ArrayList();
        List<RankItem> list = this.f12087c;
        if (list != null && !list.isEmpty()) {
            for (int size = this.f12087c.size() - 1; size >= 0; size--) {
                if (((RankItem) this.f12087c.get(size)).mo42844a() == 0) {
                    arrayList.add(((RankItem) this.f12087c.get(size)).mo42846b());
                }
            }
        }
        return arrayList;
    }

    public void onDecodeSerialData() {
        this.f12085a = readInt32();
        this.f12086b = readInt64();
        this.f12087c = new ArrayList();
        int readInt32 = readInt32();
        for (int i = 0; i < readInt32; i++) {
            RankItem bqk = new RankItem();
            bqk.decodeSerialStream(getTempInput());
            this.f12087c.add(bqk);
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Season)) {
            return false;
        }
        Season bql = (Season) obj;
        if (this.f12087c.size() != bql.mo42853b().size()) {
            return false;
        }
        for (int i = 0; i < this.f12087c.size(); i++) {
            if (!((RankItem) this.f12087c.get(i)).equals(bql.mo42853b().get(i))) {
                return false;
            }
        }
        return true;
    }
}

