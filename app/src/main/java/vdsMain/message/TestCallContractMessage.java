package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;

public class TestCallContractMessage extends VMessage {

    public TestCallContractMessage(@NonNull Wallet izVar) {
        super(izVar, "cctr");
    }

    @Override
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {

    }

    @Override
    public void onDecodeSerialData() throws IOException {
        super.onDecodeSerialData();
    }
}
