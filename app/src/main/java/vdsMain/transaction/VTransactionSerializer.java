package vdsMain.transaction;

import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.DataTypeToolkit;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

//bqh
public class VTransactionSerializer {
    //m10302a
    public static void writeVTransactionToStreamWriter(VTransactionInterface vTransactionInterface, StreamWriter streamWriter) throws IOException {
        streamWriter.writeUInt32T(vTransactionInterface.getVersion());
        streamWriter.write((byte) vTransactionInterface.getFlag());
        TransactionSerializer.writeTxInList((TransactionInteface) vTransactionInterface, streamWriter);
        TransactionSerializer.writeTxOutList((TransactionInteface) vTransactionInterface, streamWriter);
        CScriptWitness cScriptWitness = new CScriptWitness();
        for (TxIn txIn : vTransactionInterface.getSelfTxInList()) {
            CScriptWitness tempScriptWitness = txIn.getCScriptWitness();
            if (tempScriptWitness == null) {
                cScriptWitness.serialToStream(streamWriter);
            } else {
                tempScriptWitness.serialToStream(streamWriter);
            }
        }
        streamWriter.writeUInt32T(vTransactionInterface.getLockTime());
        streamWriter.writeUInt32T(vTransactionInterface.getExp());
        streamWriter.writeUInt64(vTransactionInterface.getVBalance());
        writeSpendDescriptionList(vTransactionInterface.getSpendDescriptionList(), streamWriter);
        writeOutputDescriptionList(vTransactionInterface.getOutputDescriptionList(), streamWriter);
        byte[] bsignBytes = vTransactionInterface.getBsign();
        if (bsignBytes == null || bsignBytes.length == 0) {
            streamWriter.write(new byte[64]);
        } else if (bsignBytes.length == 64) {
            streamWriter.write(bsignBytes);
        } else {
            throw new IOException(String.format(Locale.getDefault(), "invalidate length of binding sig: %d", new Object[]{Integer.valueOf(bsignBytes.length)}));
        }
    }

    //m10303a
    public static void decodeFromSeriableData(VTransactionInterface vTransactionInterface, SeriableData seriableData) {
        long version= seriableData.readUInt32();
        vTransactionInterface.setVersion(version);
        vTransactionInterface.setFlag((int) seriableData.readByte() & 0xff);
        TransactionSerializer.decodeTxInList(vTransactionInterface, seriableData);
        TransactionSerializer.decodeToTxOutList(vTransactionInterface, seriableData);
        for (TxIn txIn : vTransactionInterface.getSelfTxInList()) {
            CScriptWitness cScriptWitness = new CScriptWitness();
            cScriptWitness.decodeSerialStream(seriableData);
            txIn.setCScriptWitness(cScriptWitness, true);
        }
        vTransactionInterface.checkAndSetLockTime(seriableData.readUInt32());
        vTransactionInterface.setExp(seriableData.readUInt32());
        vTransactionInterface.setVBalance(seriableData.readUInt64().longValue());
        vTransactionInterface.setSpendDescriptionList(m10301a(seriableData), false);
        vTransactionInterface.setOutputDescriptionList(m10305b(seriableData), false);
        byte[] readBytes = seriableData.readBytes(64);
        if (!DataTypeToolkit.isZeroBytes(readBytes)) {
            vTransactionInterface.setSigt(readBytes, false);
        } else {
            vTransactionInterface.setSigt((byte[]) null, false);
        }
        vTransactionInterface.updateTxidByContent();
    }

    //m10304a
    public static void writeSpendDescriptionList(List<SpendDescription> list, StreamWriter streamWriter) throws IOException {
        streamWriter.writeObjectList(list);
    }

    /* renamed from: a */
    public static List<SpendDescription> m10301a(SeriableData seriableData) {
        if (seriableData == null) {
            return null;
        }
        int b = seriableData.readVariableInt().getIntValue();
        if (b <= 0) {
            return null;
        }
        Vector vector = new Vector(b);
        for (int i = 0; i < b; i++) {
            SpendDescription brl = new SpendDescription();
            brl.decodeSerialStream(seriableData);
            vector.add(brl);
        }
        return vector;
    }

    //m10306b
    public static void writeOutputDescriptionList(List<OutputDescription> list, StreamWriter streamWriter) throws IOException {
        streamWriter.writeObjectList(list);
    }

    /* renamed from: b */
    public static List<OutputDescription> m10305b(SeriableData seriableData) {
        if (seriableData == null) {
            return null;
        }
        int b = seriableData.readVariableInt().getIntValue();
        if (b <= 0) {
            return null;
        }
        Vector vector = new Vector(b);
        for (int i = 0; i < b; i++) {
            OutputDescription brj = new OutputDescription();
            brj.decodeSerialStream(seriableData);
            vector.add(brj);
        }
        return vector;
    }
}
