package vdsMain;

import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.IOException;

public interface CTxDestination {
    //mo9422a
    CTxDestinationType getCTxDestinationType();

    //mo9423a
    void writeTypeAndData(StreamWriter streamWriter) throws IOException;

    /* renamed from: a */
    void mo9424a(SeriableData seriableData, boolean z) throws IOException;

    //mo9425b
    CTxDestination clone();

    //mo9426c
    //915 mo9448c
    String getHash();

    byte[] data();

    boolean isNull();
}