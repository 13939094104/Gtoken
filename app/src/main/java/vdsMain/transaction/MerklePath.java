package vdsMain.transaction;

import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.DataTypeToolkit;
import vdsMain.Util;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

//bqx
public class MerklePath extends SeriableData {

    /* renamed from: a */
    public Vector<Vector<Boolean>> f12136a = new Vector<>();

    /* renamed from: b */
    public Vector<Boolean> f12137b = new Vector<>();

    public MerklePath() {
    }

    public MerklePath(Vector<Vector<Boolean>> vector, Vector<Boolean> vector2) {
        this.f12136a = vector;
        this.f12137b = vector2;
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        if (this.f12136a.size() == this.f12137b.size()) {
            Vector vector = new Vector(this.f12136a.size());
            Iterator it = this.f12136a.iterator();
            while (it.hasNext()) {
                vector.add(Util.m10406a((List<Boolean>) (Vector) it.next()));
            }
            long b = Util.m10409b(this.f12137b);
            new ByteDataListListSerilizer().mo44650a(vector, streamWriter);
            writeUInt64(b);
            return;
        }
        throw new IOException("Auth path size must same as index size.");
    }

    public void onDecodeSerialData() {
        Vector vector = new Vector();
        new ByteDataListListSerilizer().mo44651a(vector, new VectorCreator(), this);
        long longValue = readUInt64().longValue();
        int size = vector.size();
        Iterator it = vector.iterator();
        int i = 0;
        while (it.hasNext()) {
            this.f12136a.add(Util.m10407a((Vector) it.next()));
            this.f12137b.add(Boolean.valueOf(DataTypeToolkit.m11496a((longValue >> ((size - 1) - i)) & 1)));
            i++;
        }
    }
}
