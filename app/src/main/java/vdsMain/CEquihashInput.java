package vdsMain;

import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import net.bither.bitherj.utils.Utils;
import vdsMain.block.BlockHeader;
import vdsMain.block.VBlockHeader;

import java.io.IOException;

//bpm
public class CEquihashInput extends VBlockHeader {
    public CEquihashInput(VBlockHeader bje) {
        super((BlockHeader) bje);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeUInt32T(this.blockVersion);
        streamWriter.writeUInt256(this.preBlockHash);
        streamWriter.writeUInt256(this.merkelRootHash);
        streamWriter.writeUInt256(this.hashFinalSaplingRoot);
        streamWriter.writeUInt64(this.mVibPool);
        streamWriter.writeUInt32T(this.blockTime);
        streamWriter.writeUInt32T(this.bits);
        streamWriter.writeUInt256(this.hashStateRoot);
        streamWriter.writeUInt256(this.hashUTXORoot);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void decodeSerialData(SeriableData seriableData) {
        byte[] readBytes = seriableData.readBytes(getBlockHeaderByteLength());
        this.blockVersion = net.bither.bitherj.utils.Utils.m3444a(readBytes, 0);
        this.preBlockHash.setData(readBytes, 4);
        this.merkelRootHash.setData(readBytes, 36);
        this.hashFinalSaplingRoot = UInt256.unSerialUInt256(this.hashFinalSaplingRoot, readBytes, 68);
        this.mVibPool = net.bither.bitherj.utils.Utils.m3452b(readBytes, 100);
        this.blockTime = net.bither.bitherj.utils.Utils.m3444a(readBytes, 108);
        this.bits = Utils.m3444a(readBytes, 112);
        this.hashStateRoot = UInt256.unSerialUInt256(this.hashStateRoot, readBytes, 116);
        this.hashUTXORoot = UInt256.unSerialUInt256(this.hashUTXORoot, readBytes, 148);
        this.nonce = UInt256.unSerialUInt256(this.nonce, readBytes, 180);
        this.solutionBytes = null;
        mo44301l();
    }
}
