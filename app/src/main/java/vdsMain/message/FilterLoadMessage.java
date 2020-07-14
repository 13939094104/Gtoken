package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.wallet.Wallet;

//bkz
public class FilterLoadMessage extends BloomFilter {
    public FilterLoadMessage(@NonNull Wallet wallet) {
        super(wallet);
        this.typeString = "filterload";
    }
}
