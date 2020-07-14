package vdsMain.message;

import androidx.annotation.NonNull;
import bitcoin.BaseBlob;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import vdsMain.Log;
import vdsMain.block.BlockHeader;
import vdsMain.block.BlockInfo;
import vdsMain.block.CValidationState;
import vdsMain.peer.HeaderMessageInterface;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

//blv
public class HeadersMessage extends VMessage implements HeaderMessageInterface {

    //f11893a
    private Vector<BlockHeader> blockHeaderVector = new Vector<>();

    public HeadersMessage(@NonNull Wallet izVar) {
        super(izVar, "headers");
    }

    public void onDecodeSerialData() throws IOException {
        super.onDecodeSerialData();
        int count = readVariableInt().getIntValue();
        this.blockHeaderVector.clear();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                BlockHeader blockHeader = this.wallet.getSelfWalletHelper().getNewBlockHeader();
                blockHeader.mo44290a(true);
                blockHeader.decodeSerialItem(getTempInput());
                this.blockHeaderVector.add(blockHeader);
            }
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        Vector<BlockHeader> vector = this.blockHeaderVector;
        if (vector == null) {
            streamWriter.writeVariableInt(0);
            return;
        }
        Iterator it = vector.iterator();
        while (it.hasNext()) {
            ((BlockHeader) it.next()).mo44659c(streamWriter);
        }
    }

    //mo42570a
    public void logHeadersInfo() {
        StringBuffer stringBuffer = new StringBuffer("headers: ");
        Vector<BlockHeader> vector = this.blockHeaderVector;
        if (vector == null) {
            stringBuffer.append("0");
        } else {
            stringBuffer.append(Integer.toString(vector.size()));
            if (!this.blockHeaderVector.isEmpty()) {
                BlockHeader firstElement = (BlockHeader) this.blockHeaderVector.firstElement();
                BlockHeader lastElement = (BlockHeader) this.blockHeaderVector.lastElement();
                stringBuffer.append("\nfirst header");
                addBlockHeaderInfoToSting(stringBuffer, firstElement);
                if (lastElement != firstElement) {
                    stringBuffer.append("\nlast header");
                    addBlockHeaderInfoToSting(stringBuffer, lastElement);
                }
            }
        }
        Log.m11473a((Object) this, stringBuffer.toString());
    }

    //m9866a
    private void addBlockHeaderInfoToSting(StringBuffer stringBuffer, BlockHeader blockHeader) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n[\nhash=");
        sb.append(blockHeader.getBlockHash().hashString());
        stringBuffer.append(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("\nprev_hash=");
        sb2.append(blockHeader.getPreBlockHash().hashString());
        stringBuffer.append(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("\nmerkel=");
        sb3.append(blockHeader.getMerkelRootHash().hashString());
        stringBuffer.append(sb3.toString());
        stringBuffer.append("\n]");
    }

    //mo42574e
    public Vector<BlockHeader> getBlockHeaderVector() {
        return this.blockHeaderVector;
    }

    //mo42573d
    public int getBlockHeaderVectorSize() {
        Vector<BlockHeader> vector = this.blockHeaderVector;
        if (vector == null) {
            return 0;
        }
        return vector.size();
    }

    public boolean isRejectMessage(CValidationState cValidationState, UInt256 uInt256) {
        return false;
    }

    //mo42571a
    public boolean isRejectMessageOld(CValidationState cValidationState, UInt256 uInt256) {
        Vector<BlockHeader> vector = this.blockHeaderVector;
        if (vector == null || vector.isEmpty()) {
            return false;
        }
        BlockInfo blockInfo = this.wallet.checkAndGetBlockInfo(((BlockHeader) this.blockHeaderVector.get(0)).getPreBlockHash());
        if (blockInfo == null) {
            return true;
        }
        int blockNo = blockInfo.getBlockNo();
        UInt256 uInt2562 = null;
        Iterator it = this.blockHeaderVector.iterator();
        while (it.hasNext()) {
            BlockHeader blockHeader = (BlockHeader) it.next();
            blockNo++;
            blockHeader.setBlockNo(blockNo);
            if (uInt2562 != null && !blockHeader.getPreBlockHash().equals(uInt2562)) {
                uInt256.set((BaseBlob) blockHeader.getBlockHash());
                return true;
            } else if (!blockHeader.checkBlockVaild(cValidationState, true)) {
                uInt256.set((BaseBlob) blockHeader.getBlockHash());
                return true;
            } else {
                uInt2562 = blockHeader.getBlockHash();
            }
        }
        return false;
    }



}
