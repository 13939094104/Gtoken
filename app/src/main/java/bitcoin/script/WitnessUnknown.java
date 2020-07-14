package bitcoin.script;

import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import net.bither.bitherj.utils.Utils;
import vdsMain.CTxDestination;
import vdsMain.CTxDestinationType;
import vdsMain.DataTypeToolkit;
import vdsMain.StringToolkit;
import vdsMain.transaction.Script;

import java.io.IOException;
import java.util.Arrays;

public class WitnessUnknown implements CTxDestination {

    /* renamed from: a */
    public int f417a;

    /* renamed from: b */
    public int f418b;

    /* renamed from: c */
    public byte[] f419c = new byte[40];

    /* renamed from: d */
    protected int f420d;

    /* renamed from: a */
    public CTxDestinationType getCTxDestinationType() {
        return CTxDestinationType.WITNESS_UNKNOWN;
    }

    public byte[] data() {
        return mo9565d();
    }

    /* renamed from: c */
    public String getHash() {
        byte[] bArr = new byte[49];
        bArr[0] = (byte) getCTxDestinationType().getValue();
        this.f417a = Utils.m3456c(bArr, 1);
        this.f418b = Utils.m3456c(bArr, 5);
        System.arraycopy(bArr, 9, this.f419c, 0, 40);
        return StringToolkit.bytesToString(bArr);
    }

    /* renamed from: b */
    public CTxDestination clone() {
        WitnessUnknown witnessUnknown = new WitnessUnknown();
        witnessUnknown.f420d = this.f420d;
        witnessUnknown.f418b = this.f418b;
        witnessUnknown.f417a = this.f417a;
        witnessUnknown.f419c = DataTypeToolkit.bytesCopy(this.f419c);
        return witnessUnknown;
    }

    public boolean isNull() {
        byte[] bArr = this.f419c;
        return bArr == null || bArr.length == 0 || DataTypeToolkit.isZeroBytes(bArr);
    }

    /* access modifiers changed from: protected */
    /* renamed from: d */
    public byte[] mo9565d() {
        byte[] bArr = new byte[(this.f418b + 1)];
        bArr[0] = (byte) Script.encodeOP_N(this.f417a);
        System.arraycopy(this.f419c, 0, bArr, 1, this.f418b);
        return bArr;
    }

    /* access modifiers changed from: protected */
    /* renamed from: e */
    public void mo9566e() {
        byte[] bArr = new byte[52];
        Utils.uint32ToByteArrayLE((long) getCTxDestinationType().getValue(), bArr, 0);
        Utils.uint32ToByteArrayLE((long) this.f417a, bArr, 4);
        Utils.uint32ToByteArrayLE((long) this.f418b, bArr, 8);
        System.arraycopy(this.f419c, 0, bArr, 12, 40);
        this.f420d = Arrays.hashCode(bArr);
    }

    public int hashCode() {
        return this.f420d;
    }

    /* renamed from: a */
    public void writeTypeAndData(StreamWriter streamWriter) throws IOException {
        streamWriter.write((byte) getCTxDestinationType().getValue());
        streamWriter.writeUInt32T((long) this.f417a);
        streamWriter.writeUInt32T((long) this.f418b);
        streamWriter.write(this.f419c);
    }

    /* renamed from: a */
    public void mo9424a(SeriableData seriableData, boolean z) {
        if (z) {
            seriableData.readByte();
        }
        this.f417a = (int) seriableData.readUInt32();
        this.f418b = (int) seriableData.readUInt32();
        seriableData.readBytes(this.f419c);
        mo9566e();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof WitnessUnknown) || obj.hashCode() != this.f420d) {
            return false;
        }
        WitnessUnknown witnessUnknown = (WitnessUnknown) obj;
        if (this.f417a == witnessUnknown.f417a && this.f418b == witnessUnknown.f418b) {
            return Arrays.equals(this.f419c, witnessUnknown.f419c);
        }
        return false;
    }
}
