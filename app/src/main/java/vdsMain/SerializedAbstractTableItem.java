package vdsMain;

import android.content.Context;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.table.AbstractTable;
import vdsMain.table.AbstractTableItem;

import java.io.IOException;
import java.io.OutputStream;

public abstract class SerializedAbstractTableItem extends AbstractTableItem {

    /* renamed from: lp$a */
    /* compiled from: SerializedAbstractTableItem */
    public class C3899a extends SeriableData {
        public C3899a() {
        }

        public C3899a(ByteBuffer byteBuffer) {
            super(byteBuffer);
        }

        public void writeSerialData(StreamWriter streamWriter) throws IOException {
            if (streamWriter != null) {
                SerializedAbstractTableItem.this.writeSerialData(streamWriter);
            } else {
                SerializedAbstractTableItem.this.writeSerialData((StreamWriter) this);
            }
        }

        public void onDecodeSerialData() {
            SerializedAbstractTableItem.this.decodeSerialData((SeriableData) this);
        }
    }

    /* access modifiers changed from: protected */
    //mo40988a
    public abstract void writeSerialData(StreamWriter streamWriter) throws IOException;

    /* access modifiers changed from: protected */
    //mo40989a
    public abstract void decodeSerialData(SeriableData seriableData);

    public SerializedAbstractTableItem() {
    }

    public SerializedAbstractTableItem(SerializedAbstractTableItem lpVar) {
        super(lpVar);
    }

    public SerializedAbstractTableItem(Context context, AbstractTable fxVar) {
        super(context, fxVar);
    }

    /* renamed from: c */
    public void mo44659c(StreamWriter streamWriter) {
        try {
            new C3899a().serialToStream(streamWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* renamed from: a */
    public void mo44656a(OutputStream outputStream) {
        try {
            new C3899a().serialToStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //mo44653X
    public byte[] serialSelfToBytes() throws IOException {
            return new C3899a().serialToStream();
    }

    /* renamed from: a */
    public long mo44655a(String str) throws IOException {
        byte[] bytes=StringToolkit.m11526a(str);
        return mo44657b(bytes, 0);
    }

    //910 mo44741c
    public long mo44658c(SeriableData seriableData) {
        return new C3899a().decodeSerialStream(seriableData);
    }


    public long mo44657b(byte[] bArr, int i) throws IOException {
            return new C3899a().decodeSerialStream(bArr, i);
    }

    //mo44654a
    public long decodeSerialItem(ByteBuffer byteBuffer) {
        return new C3899a().decodeSerialStream(byteBuffer);
    }
}
