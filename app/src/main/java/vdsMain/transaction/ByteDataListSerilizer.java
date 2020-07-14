package vdsMain.transaction;

import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.IOException;
import java.util.Collection;

public class ByteDataListSerilizer {
    /* renamed from: a */
    public static void m13196a(Collection<Byte> collection, StreamWriter streamWriter) throws IOException {
        if (streamWriter.writeCollectionSize(collection) >= 1) {
            for (Byte byteValue : collection) {
                streamWriter.write(byteValue.byteValue());
            }
        }
    }

    /* renamed from: a */
    public static void m13197a(Collection<Byte> collection, SeriableData seriableData) {
        for (int b = seriableData.readVariableInt().getIntValue(); b > 0; b--) {
            collection.add(Byte.valueOf(seriableData.readByte()));
        }
    }
}