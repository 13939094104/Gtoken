package vdsMain;

import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.IOException;
import java.util.Locale;

public class CFeeRate extends SeriableData {

    /* renamed from: a */
    public long f12778a;

    public CFeeRate(long j) {
        this.f12778a = j;
    }

    /* renamed from: a */
    public long mo43715a(long j) {
        long j2 = this.f12778a;
        long j3 = (j * j2) / 1000;
        return (j3 != 0 || j2 <= 0) ? j3 : j2;
    }

    /* renamed from: a */
    public long mo43714a() {
        return mo43715a(1000);
    }

    public String toString() {
        return String.format(Locale.getDefault(), "%d.%08d %s/kB", new Object[]{"VC", Long.valueOf(this.f12778a / 100000000), Long.valueOf(this.f12778a % 100000000)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CFeeRate)) {
            return false;
        }
        if (((CFeeRate) obj).f12778a != this.f12778a) {
            z = false;
        }
        return z;
    }

    public void writeSerialData(StreamWriter streamWriter) {
        try {
            streamWriter.writeUInt64(this.f12778a);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onDecodeSerialData() {
        this.f12778a = readUInt64().longValue();
    }
}
