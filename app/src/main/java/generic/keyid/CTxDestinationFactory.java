package generic.keyid;

import bitcoin.CNoDestination;
import bitcoin.script.WitnessUnknown;
import bitcoin.script.WitnessV0KeyHash;
import bitcoin.script.WitnessV0ScriptHash;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import net.bither.bitherj.utils.Utils;
import vdsMain.*;
import vdsMain.transaction.CScriptID;

import java.io.IOException;
import java.util.Arrays;

public  final class CTxDestinationFactory {


    public static native CTxDestination DecodeWitnessTxDestination(String str, String str2);

    public static int m908a(CTxDestination cTxDestination) {
        int i;
        byte[] data = cTxDestination.data();
        if (data == null) {
            i = 4;
        } else {
            i = data.length + 4;
        }
        byte[] bArr = new byte[i];
        int a = cTxDestination.getCTxDestinationType().getValue();
        Utils.m3446a(a, bArr, 0);
        if (data != null) {
            System.arraycopy(data, 0, bArr, 4, data.length);
        }
        return (a * 31) + Arrays.hashCode(bArr);
    }

    public static CTxDestination m911a(String str) throws IOException {
        return getDesByBytes(StringToolkit.m11526a(str));
    }

    public static CTxDestination m910a(SeriableData seriableData) throws IOException {
        CTxDestination a = getDesByFlag((int) seriableData.readByte());
        a.mo9424a(seriableData, false);
        return a;
    }

    public static void m914a(CTxDestination oVar, StreamWriter streamWriter) throws IOException {
        if (oVar == null) {
            new CNoDestination().writeTypeAndData(streamWriter);
        } else {
            oVar.writeTypeAndData(streamWriter);
        }
    }


    //915 m916a
    //m913a
    public static CTxDestination getDesByBytes(byte[] bArr) throws IOException {
        if (bArr == null || bArr.length < 1) {
            return new CNoDestination();
        }
        CTxDestination des = getDesByFlag((int) bArr[0]);
        des.mo9424a(new DummySeriableData(new ByteBuffer(bArr)), true);
        return des;
    }

    //m909a
    private static CTxDestination getDesByFlag(int i) {
        switch (CTxDestinationType.getDesType(i & 255)) {
            case KEYID:
                return new CKeyID();
            case SCRIPTID:
                return new CScriptID();
            case ANONYMOUST_KEY:
                return new SaplingPaymentAddress();
            case WITNESS_V0_KEY_HASH:
                return new WitnessV0KeyHash();
            case WITNESS_V0_SCRIPT_HASH:
                return new WitnessV0ScriptHash();
            case WITNESS_UNKNOWN:
                return new WitnessUnknown();
            case CONTRACT:
                return new CTxDestinationContract();
            default:
                return new CNoDestination();
        }
    }
}
