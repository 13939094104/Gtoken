package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.ByteBuffer;
import vdsMain.wallet.Wallet;

public abstract class MessageFactory {

    //f13188a
    protected Wallet wallet;

    /* renamed from: a */
    public abstract Message getUnKonwnMessage(String str);

    /* renamed from: a */
    public abstract Message getMessageByCommand(String str, Wallet izVar);

    public MessageFactory(@NonNull Wallet izVar) {
        this.wallet = izVar;
    }

    //mo44402a
    public Message getMessage(MessageHeader messageHeader, ByteBuffer byteBuffer) {
        Message message = getMessageByCommand(messageHeader.getCommand(), this.wallet);
        if (message == null) {
            message = getUnKonwnMessage(messageHeader.getCommand());
        }
        message.mo44393a(messageHeader, byteBuffer);
        return message;
    }
}