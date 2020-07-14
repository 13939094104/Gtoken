package vdsMain.message;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import vdsMain.block.BlockHeader;
import vdsMain.model.PartialMerkleTree;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.List;

//bmb
public class MerkelBlockMessage extends VMessage implements MerkelBlockMessageInterface {

    /* renamed from: a */
    private BlockHeader blockHeader = null;

    /* renamed from: b */
    private PartialMerkleTree f11906b = null;

    public MerkelBlockMessage(@NonNull Wallet izVar) {
        super(izVar, "merkleblock");
    }

    public void onDecodeSerialData() throws IOException {
        super.onDecodeSerialData();
        this.blockHeader = this.wallet.getSelfWalletHelper().getNewBlockHeader();
        this.blockHeader.decodeSerialItem(getTempInput());
        this.f11906b = new PartialMerkleTree();
        this.f11906b.decodeSerialStream(getTempInput());
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        this.blockHeader.mo44659c(streamWriter);
        this.f11906b.serialToStream(streamWriter);
    }

    /* renamed from: b */
    public List<UInt256> mo42581b() {
        PartialMerkleTree aaVar = this.f11906b;
        if (aaVar == null) {
            return null;
        }
        try {
            return aaVar.mo62a();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: a */
    public BlockHeader mo42580a() {
        return this.blockHeader;
    }
}
