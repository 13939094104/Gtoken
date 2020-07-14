package vdsMain.observer;

import com.vtoken.vdsecology.vcash.InitInfo;
import vdsMain.wallet.WalletType;

public interface CoreEventObserver {
    //915 mo39698a
    void mo39596a();

    //mo39597a
    //915 mo39699a
    void handleInitInfoEnum(InitInfo isVar);

    //915 mo39700a
    void mo39598a(WalletType jeVar);

    //mo39599a
    //915 mo39701a
    void onResynched(boolean z);

    //915 mo39702b
    void mo39600b();
}