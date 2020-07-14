package bitcoin;

import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.CTxDestination;
import vdsMain.CTxDestinationType;

import java.io.IOException;
import java.util.Locale;

public class CNoDestination implements CTxDestination {
    /* renamed from: b */
    public CTxDestination clone() {
        return null;
    }

    public byte[] data() {
        return null;
    }

    public boolean isNull() {
        return true;
    }

    /* renamed from: a */
    public CTxDestinationType getCTxDestinationType() {
        return CTxDestinationType.CNODESTINATION;
    }

    /* renamed from: c */
    public String getHash() {
        return String.format(Locale.US, "%02x", new Object[]{Integer.valueOf(getCTxDestinationType().getValue())});
    }

    /* renamed from: a */
    public void writeTypeAndData(StreamWriter streamWriter) throws IOException {
        streamWriter.write(new byte[]{(byte) getCTxDestinationType().getValue()});
    }

    /* renamed from: a */
    public void mo9424a(SeriableData seriableData, boolean z) {
        if (z) {
            seriableData.readByte();
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return obj instanceof CNoDestination;
    }
}
