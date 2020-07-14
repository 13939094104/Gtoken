package vdsMain.transaction;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

//bqe
public class VCMutableTransaction extends ZCMutableTransaction implements VTransactionInterface {
    public VCMutableTransaction(@NonNull CMutableTransaction dfVar) {
        super(dfVar);
        if (dfVar != this) {
        }
    }

    public VCMutableTransaction(Transaction dhVar) {
        super(dhVar);
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        VTransactionSerializer.writeVTransactionToStreamWriter((VTransactionInterface) this, streamWriter);
    }

    public void onDecodeSerialData() {
        VTransactionSerializer.decodeFromSeriableData((VTransactionInterface) this, (SeriableData) this);
    }

    /* renamed from: i_ */
    @NotNull
    public UInt256 updateTxidByContent() {
        this.txId.clear();
        try {
            CHashWriter kVar = new CHashWriter();
            VTransactionSerializer.writeVTransactionToStreamWriter((VTransactionInterface) this, (StreamWriter) kVar);
            mo43214a(kVar.GetHash());
        } catch (IOException e) {
            e.printStackTrace();
            mo43214a(UInt256.empty());
        }
        return this.txId;
    }

    /* renamed from: b */
    public CMutableTransaction clone() {
        return new VCMutableTransaction((CMutableTransaction) this);
    }
}
