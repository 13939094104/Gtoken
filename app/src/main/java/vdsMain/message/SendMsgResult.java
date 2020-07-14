package vdsMain.message;

import vdsMain.peer.Peer;

//bpk
public class SendMsgResult {
    //f12026a
    boolean isSuccess;

    //f12027b
    Peer peer;

    public SendMsgResult(boolean z, Peer lhVar) {
        this.isSuccess = z;
        this.peer = lhVar;
    }

    //mo42702a
    public boolean getIsSuccess() {
        return this.isSuccess;
    }

    //mo42703b
    public Peer getPeer() {
        return this.peer;
    }
}
