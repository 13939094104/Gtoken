package vdsMain.message;

import androidx.annotation.NonNull;
import vdsMain.peer.Inv;
import vdsMain.wallet.Wallet;

import java.util.List;

//bmc
public class NotFoundMessage extends InvMessage implements NotFoundMessageInteface {

    public NotFoundMessage(@NonNull Wallet izVar) {
        super(izVar);
        this.typeString = "notfound";
    }

    @Override
    public List<Inv> mo44403a() {
        return null;
    }

    @Override
    public void mo44404a(Inv inv) {

    }

    @Override
    public boolean mo44405b() {
        return false;
    }
}
