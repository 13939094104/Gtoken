package vdsMain.peer;

import bitcoin.UInt256;
import vdsMain.block.BlockHeader;
import vdsMain.block.CValidationState;

import java.util.List;

public interface HeaderMessageInterface {
    //910 mo42630a
    void logHeadersInfo();

    //910 mo42631a
    boolean isRejectMessage(CValidationState atVar, UInt256 uInt256);

    //910 mo42633d
    int getBlockHeaderVectorSize();

    //910 mo42634e
    List<BlockHeader> getBlockHeaderVector();
}