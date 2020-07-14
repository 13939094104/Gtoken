package vdsMain.transaction;

import androidx.annotation.NonNull;
import bitcoin.BaseBlob;
import bitcoin.UInt256;
import bitcoin.script.CScript;
import com.google.common.primitives.UnsignedBytes;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import generic.utils.AddressUtils;
import net.bither.bitherj.exception.ScriptException;
import vdsMain.CTxDestination;
import vdsMain.DataTypeToolkit;
import vdsMain.wallet.ChainParams;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//bqi
public class VTxOut extends TxOut {

    //f12077h
    private UInt256 dataHash;

    public VTxOut(@NonNull Wallet izVar) {
        super(izVar);
    }

    public VTxOut(TxOut dnVar) {
        super(dnVar);
        if (dnVar instanceof VTxOut) {
            mo42835b(((VTxOut) dnVar).dataHash);
        }
    }

    public VTxOut(@NonNull Wallet izVar, long j, int i, CScript cScript, UInt256... uInt256Arr) throws ScriptException {
        super(izVar, j, i, new Script(cScript));
        if (uInt256Arr.length > 0) {
            mo42835b(uInt256Arr[0]);
        }
    }

    /* renamed from: p */
    public UInt256 mo42842p() {
        return this.dataHash;
    }

    /* renamed from: b */
    public void mo42835b(UInt256 uInt256) {
        if (uInt256 == null) {
            this.dataHash = null;
            return;
        }
        UInt256 uInt2562 = this.dataHash;
        if (uInt2562 == null) {
            this.dataHash = new UInt256();
            this.dataHash.set((BaseBlob) uInt256);
            return;
        }
        uInt2562.set((BaseBlob) uInt256);
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeUInt64(this.mSatoshi);
        streamWriter.writeBytes(new byte[]{(byte)mFlag});
        if (this.script != null) {
            byte[] chunksBytes = this.script.getScriptChunksBytes();
            if (chunksBytes == null) {
                streamWriter.writeVariableInt(0);
            } else {
                streamWriter.writeVariableInt((long) chunksBytes.length);
                streamWriter.writeBytes(chunksBytes);
            }
        } else {
            streamWriter.writeVariableInt(0);
        }
        streamWriter.writeUInt256(this.dataHash);
    }

    public void onDecodeSerialData() throws IOException {
        this.mSatoshi = readUInt64().longValue();
        this.mFlag = (short) (readByte() & UnsignedBytes.MAX_VALUE);
        int b = readVariableInt().getIntValue();
        if (b > 0) {
            try {
                this.script = new Script(readBytes(b), 0, b);
            } catch (ScriptException e) {
                e.printStackTrace();
                throw new IOException(e);
            }
        }
        if (this.dataHash == null) {
            this.dataHash = new UInt256();
        }
        this.dataHash.decodeSerialStream((SeriableData) this);
        if (this.dataHash.isNull()) {
            this.dataHash = null;
        }
        reComputeIndexAndHash();
    }

    /* renamed from: n */
    public COutPoint mo42840n() {
        return new VCOutPoint(this.txid, this.mIndex);
    }

    /* renamed from: l */
    public CTxDestination getScriptCTxDestination() {
        if (this.script == null) {
            return null;
        }
        CTxDestination j = this.script.mo43169j();
        if (j == null && this.script.getTxnOutType() == TxnOutType.TX_CREATE) {
            Transaction b = this.wallet.getTransactionFromAllMap(this.txid);
            if (b != null) {
                ContractTransaction bjp = new ContractTransaction(this.wallet);
                bjp.mo42520a(b, new boolean[0]);
                j = bjp.mo42519a();
            }
        }
        return j;
    }

    /* renamed from: i */
    public String getAddress() {
        String i = super.getAddress();
        if (i == null && this.script != null && this.script.getTxnOutType() == TxnOutType.TX_CREATE) {
            CTxDestination l = getScriptCTxDestination();
            if (l == null) {
                return null;
            }
            i = AddressUtils.getAddressString(l, (ChainParams) null);
        }
        return i;
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj) || !(obj instanceof VTxOut)) {
            return false;
        }
        return DataTypeToolkit.m11497a((Object) this.dataHash, (Object) ((VTxOut) obj).dataHash);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(super.toString());
        if (this.dataHash != null) {
            stringBuffer.append("\ndataHash: ");
            stringBuffer.append(this.dataHash.toString());
        } else {
            stringBuffer.append("\ndataHash: 0000000000000000000000000000000000000000000000000000000000000000");
        }
        return stringBuffer.toString();
    }

    /* renamed from: o */
    public TxOut clone() {
        return new VTxOut((TxOut) this);
    }
}
