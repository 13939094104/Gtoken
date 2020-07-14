package vdsMain.observer;

import vdsMain.message.Message;

public interface ASyncMessageObserver {
    void onMessageReceived(Message message);
}
