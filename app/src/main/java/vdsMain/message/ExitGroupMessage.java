package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bno
public class ExitGroupMessage extends ContractGroupMessage {
    public ExitGroupMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "psnexitg", i);
    }

    public ExitGroupMessage(@NonNull Wallet izVar, int i, String str) {
        super(izVar, "psnexitg", i, str);
    }
}
