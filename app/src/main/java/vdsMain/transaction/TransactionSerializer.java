package vdsMain.transaction;

import bitcoin.VariableInteger;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import org.apache.commons.io.FileUtils;
import vdsMain.ByteBuffer;
import vdsMain.DummySeriableData;
import vdsMain.DumpedStreamWriter;
import vdsMain.wallet.WalletHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class TransactionSerializer {
    /* renamed from: a */
    public static void m10971a(TransactionInteface diVar, SeriableData seriableData) {
        List e = diVar.getSelfTxInList();
        e.clear();
        int b = seriableData.readVariableInt().getIntValue();
        int i = 0;
        while (b > 0) {
            TxIn dlVar = new TxIn(diVar.getWallet());
            dlVar.decodeSerialStream(seriableData.getTempInput());
            int i2 = i + 1;
            dlVar.setIndex(i);
            e.add(dlVar);
            b--;
            i = i2;
        }
    }

    /* renamed from: b */
    public static void m10979b(TransactionInteface transactionInteface, SeriableData seriableData) {
        List d = transactionInteface.getSelfTxOutList();
        d.clear();
        int b = seriableData.readVariableInt().getIntValue();
        WalletHelper walletHelper = transactionInteface.getWallet().getSelfWalletHelper();
        int i = 0;
        while (b > 0) {
            TxOut h = walletHelper.getNewTxOut();
            h.decodeSerialStream(seriableData.getTempInput());
            int i2 = i + 1;
            h.setIndex(i);
            d.add(h);
            b--;
            i = i2;
        }
    }

    /* renamed from: a */
    public static void m10972a(TransactionInteface transactionInteface, OutputStream outputStream) throws IOException {
        writeTxInList(transactionInteface, (StreamWriter) new DumpedStreamWriter(outputStream));
    }

    //m10969a
    public static void writeTxInList(TransactionInteface transactionInteface, StreamWriter streamWriter) throws IOException {
        List<TxIn> txInList = transactionInteface.getSelfTxInList();
        if (txInList == null) {
            streamWriter.writeVariableInt(0);
            return;
        }
        streamWriter.writeVariableInt((long) txInList.size());
        if (!txInList.isEmpty()) {
            for (TxIn txIn : txInList) {
                streamWriter.writeObject(txIn);
            }
        }
    }

    //m10973a
    public static void seralizeToTxInList(TransactionInteface transactionInteface, byte[] bArr, int i) {
        List txInList = transactionInteface.getSelfTxInList();
        ByteBuffer byteBuffer = ByteBuffer.getNewByteBuffer(bArr, i);
        VariableInteger variableInteger = new VariableInteger();
        variableInteger.readInteger(byteBuffer);
        for (int b = variableInteger.getIntValue(); b > 0; b--) {
            TxIn txIn = new TxIn(transactionInteface.getWallet());
            txIn.decodeSerialStream(byteBuffer);
            txInList.add(txIn);
        }
    }

    //m10983c
    public static void decodeTxInList(TransactionInteface transactionInteface, SeriableData seriableData) {
        List txInList = transactionInteface.getSelfTxInList();
        int count=seriableData.readVariableInt().getIntValue();
        for (int length =count ; length > 0; length--) {
            TxIn txIn = new TxIn(transactionInteface.getWallet());
            txIn.decodeSerialStream(seriableData);
            txInList.add(txIn);
        }
    }

    /* renamed from: b */
    public static void m10981b(TransactionInteface diVar, byte[] bArr, int i) {
        List d = diVar.getSelfTxOutList();
        d.clear();
        if (bArr != null && bArr.length - i > 0) {
            WalletHelper A = diVar.getWallet().getSelfWalletHelper();
            ByteBuffer a = ByteBuffer.getNewByteBuffer(bArr, i);
            VariableInteger variableInteger = new VariableInteger();
            variableInteger.readInteger(a);
            int b = variableInteger.getIntValue();
            int i2 = 0;
            while (b > 0) {
                TxOut h = A.getNewTxOut();
                h.decodeSerialStream(a);
                h.setMTxid(diVar.getTxId());
                h.setIndex(i2);
                d.add(h);
                b--;
                i2++;
            }
        }
    }

    //m10984d
    public static void decodeToTxOutList(TransactionInteface transactionInteface, SeriableData seriableData) {
        List txOutList = transactionInteface.getSelfTxOutList();
        txOutList.clear();
        WalletHelper walletHelper = transactionInteface.getWallet().getSelfWalletHelper();
        int count = seriableData.readVariableInt().getIntValue();
        int i = 0;
        while (count > 0) {
            TxOut txOut = walletHelper.getNewTxOut();
            txOut.decodeSerialStream(seriableData);
            txOut.setMTxid(transactionInteface.getTxId());
            txOut.setIndex(i);
            txOutList.add(txOut);
            count--;
            i++;
        }
    }

    /* renamed from: b */
    public static void m10980b(TransactionInteface diVar, OutputStream outputStream) throws IOException {
        writeTxOutList(diVar, (StreamWriter) new DumpedStreamWriter(outputStream));
    }

    //m10978b
    public static void writeTxOutList(TransactionInteface transactionInteface, StreamWriter streamWriter) throws IOException {
        List<TxOut> txOutList = transactionInteface.getSelfTxOutList();
        if (txOutList == null) {
            streamWriter.writeVariableInt(0);
            return;
        }
        streamWriter.writeVariableInt((long) txOutList.size());
        if (!txOutList.isEmpty()) {
            for (TxOut writeObject : txOutList) {
                streamWriter.writeObject(writeObject);
            }
        }
    }

    //m10976a
    public static boolean anyTxInHasCScriptWitness(TransactionInteface transactionInteface) {
        List<TxIn> txInList = transactionInteface.getSelfTxInList();
        if (txInList == null) {
            return false;
        }
        for (TxIn txIn : txInList) {
            if (txIn.hasCscriptWitness()) {
                return true;
            }
        }
        return false;
    }

    /* renamed from: a */
    public static void m10970a(TransactionInteface transactionInteface, StreamWriter streamWriter, boolean... zArr) throws IOException {
        streamWriter.writeUInt32T(transactionInteface.getVersion());
        boolean z = (transactionInteface.getVersion() & FileUtils.ONE_GB) == 0;
        if (zArr.length != 0 && !zArr[0]) {
            z = false;
        }
        char c = (!z || !anyTxInHasCScriptWitness(transactionInteface)) ? 0 : (char) 1;
        if (c != 0) {
            streamWriter.writeVariableInt(0);
            streamWriter.write(new byte[]{(byte) c});
        }
        writeTxInList(transactionInteface, streamWriter);
        writeTxOutList(transactionInteface, streamWriter);
        List e = transactionInteface.getSelfTxInList();
        if (!((c & 1) == 0 || e == null)) {
            m10974a(e, streamWriter);
        }
        streamWriter.writeUInt32T(transactionInteface.getLockTime());
    }

    /* renamed from: a */
    public static void m10975a(List<TxIn> list, OutputStream outputStream) throws IOException {
        if (list == null || list.isEmpty()) {
            outputStream.write(new VariableInteger(0).encodeNativeValue());
            return;
        }
        outputStream.write(new VariableInteger(0).encodeNativeValue());
        CScriptWitness clVar = new CScriptWitness();
        for (TxIn dlVar : list) {
            if (dlVar.getCScriptWitness() == null) {
                clVar.serialToStream(outputStream);
            } else {
                dlVar.serialToStream(outputStream);
            }
        }
    }

    /* renamed from: a */
    public static void m10974a(List<TxIn> list, StreamWriter streamWriter) throws IOException {
        if (list != null && !list.isEmpty()) {
            for (TxIn m : list) {
                CScriptWitness m2 = m.getCScriptWitness();
                if (m2 == null) {
                    streamWriter.writeVariableInt(0);
                } else {
                    streamWriter.writeObject(m2);
                }
            }
        }
    }

    /* renamed from: b */
    public static byte[] m10982b(TransactionInteface diVar) throws IOException {
        return m10977a(diVar.getSelfTxInList());
    }

    /* renamed from: a */
    public static byte[] m10977a(List<TxIn> list) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        m10975a(list, (OutputStream) byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /* renamed from: a */
    public static List<CScriptWitness> m10968a(byte[] bArr, TransactionInteface... diVarArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        Vector vector = new Vector();
        DummySeriableData loVar = new DummySeriableData(new ByteBuffer(bArr));
        int b = loVar.readVariableInt().getIntValue();
        if (b < 1) {
            return vector;
        }
        for (int i = 0; i < b; i++) {
            CScriptWitness clVar = new CScriptWitness();
            clVar.decodeSerialStream((SeriableData) loVar);
            vector.add(clVar);
        }
        if (diVarArr.length == 0 || diVarArr[0] == null) {
            return vector;
        }
        List e = diVarArr[0].getSelfTxInList();
        if (e == null || e.isEmpty()) {
            return vector;
        }
        Iterator it = vector.iterator();
        Iterator it2 = e.iterator();
        while (it.hasNext() && it2.hasNext()) {
            CScriptWitness clVar2 = (CScriptWitness) it.next();
            TxIn dlVar = (TxIn) it2.next();
            if (!clVar2.isBytesListEmpty()) {
                dlVar.setCScriptWitness(clVar2, true);
            }
        }
        return vector;
    }

    /* renamed from: e */
    public static void m10985e(TransactionInteface diVar, SeriableData seriableData) throws IOException {
        byte b;
        diVar.setVersion(seriableData.readUInt32());
        boolean z = (diVar.getVersion() & FileUtils.ONE_GB) == 0;
        m10971a(diVar, seriableData);
        List<TxIn> e = diVar.getSelfTxInList();
        if ((e == null || e.isEmpty()) && z) {
            b = seriableData.readByte();
            if (b != 0) {
                m10971a(diVar, seriableData);
                m10979b(diVar, seriableData);
            }
        } else {
            m10979b(diVar, seriableData);
            b = 0;
        }
        if ((b & 1) != 0 && z) {
            b ^= 1;
            for (TxIn dlVar : e) {
                CScriptWitness clVar = new CScriptWitness();
                clVar.decodeSerialStream(seriableData);
                if (!clVar.isBytesListEmpty()) {
                    dlVar.setCScriptWitness(clVar, true);
                }
            }
        }
        if (b == 0) {
            diVar.checkAndSetLockTime(seriableData.readUInt32());
            diVar.updateTxidByContent();
            return;
        }
        throw new IOException("Unknown transaction optional data");
    }
}

