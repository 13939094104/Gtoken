package vdsMain.transaction;

import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.IOException;
import java.util.Collection;

public class ByteDataListListSerilizer<T> {
    /* renamed from: a */
    public void mo44650a(Collection<T> collection, StreamWriter streamWriter) throws IOException {
        if (streamWriter.writeCollectionSize(collection) >= 1) {
            for (T a : collection) {
                ByteDataListSerilizer.m13196a((Collection<Byte>) a, streamWriter);
            }
        }
    }

    /* renamed from: a */
    public void mo44651a(Collection<T> collection, CollectionCreator lnVar, SeriableData seriableData) {
        collection.clear();
        for (int b = seriableData.readVariableInt().getIntValue(); b > 0; b--) {
            ByteDataListSerilizer.m13197a((Collection) lnVar.mo44652a(), seriableData);
        }
    }
}