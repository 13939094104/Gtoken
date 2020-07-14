package vdsMain.block;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.WalletSeriablData;
import vdsMain.transaction.SaplingMerkleTree;
import vdsMain.transaction.Transaction;
import vdsMain.wallet.Wallet;
import vdsMain.wallet.WalletHelper;

import java.io.IOException;
import java.util.Vector;

//bjd
public class CMerkleTxBlockSample extends WalletSeriablData {

    //f11721a
    public UInt256 blockHash;

    //f11722b
    public Vector<Transaction> transactionVector;

    //f11723c
    public SaplingMerkleTree saplingMerkleTree;

    public CMerkleTxBlockSample(@NonNull Wallet izVar) {
        super(izVar);
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        UInt256 uInt256 = this.blockHash;
        if (uInt256 != null) {
            uInt256.serialToStream(streamWriter);
        } else {
            UInt256.empty().serialToStream(streamWriter);
        }
        streamWriter.writeOptionalObject(this.saplingMerkleTree);
        streamWriter.writeObjectList(this.transactionVector);
    }

    public void onDecodeSerialData() {
        try {
            this.blockHash = readUint256();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.saplingMerkleTree = (SaplingMerkleTree) readOptionalObject(SaplingMerkleTree.class);
        int b = readVariableInt().getIntValue();
        if (b < 1) {
            this.transactionVector = null;
            return;
        }
        this.transactionVector = new Vector<>(b);
        WalletHelper A = this.wallet.getSelfWalletHelper();
        for (int i = 0; i < b; i++) {
            Transaction a = A.getNewTransaction();
            a.mo44658c((SeriableData) this);
            a.setBlockHash(this.blockHash);
            this.transactionVector.add(a);
        }
    }
}
