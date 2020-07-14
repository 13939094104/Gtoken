package bitcoin.script;

import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.IOException;

public class CScriptNum extends SeriableData {

    /* renamed from: a */
    private long f416a;

    public static native byte[] serialize(long j);

    public void onDecodeSerialData() {
    }

    public CScriptNum(long j) {
        this.f416a = j;
    }

    /* renamed from: a */
    public long mo9555a(long j) {
        return this.f416a - j;
    }

    /* renamed from: b */
    public CScriptNum mo9556b(long j) {
        return new CScriptNum(j & this.f416a);
    }

    /* renamed from: a */
    public int mo9554a() {
        long j = this.f416a;
        if (j > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        if (j < -2147483648L) {
            return Integer.MIN_VALUE;
        }
        return (int) j;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj instanceof CScriptNum) {
            if (this.f416a != ((CScriptNum) obj).f416a) {
                z = false;
            }
            return z;
        } else if (obj instanceof Long) {
            if (this.f416a != ((Long) obj).longValue()) {
                z = false;
            }
            return z;
        } else if (obj instanceof Integer) {
            if (this.f416a != ((long) ((Integer) obj).intValue())) {
                z = false;
            }
            return z;
        } else if (obj instanceof Short) {
            if (this.f416a != ((long) ((Short) obj).shortValue())) {
                z = false;
            }
            return z;
        } else if (obj instanceof Character) {
            if (this.f416a != ((long) ((Character) obj).charValue())) {
                z = false;
            }
            return z;
        } else if (!(obj instanceof Byte)) {
            return false;
        } else {
            if (this.f416a != ((long) ((Byte) obj).byteValue())) {
                z = false;
            }
            return z;
        }
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.write(serialize(this.f416a));
    }
}
