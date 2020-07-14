package vdsMain;


//brh
public class ZParams extends Params {

    /* renamed from: M */
    public NetworkUpgrade[] f12162M = new NetworkUpgrade[UpgradeIndex.MAX_NETWORK_UPGRADES.mo42936a()];

    public ZParams() {
        int i = 0;
        while (true) {
            NetworkUpgrade[] brfArr = this.f12162M;
            if (i < brfArr.length) {
                brfArr[i] = new NetworkUpgrade();
                i++;
            } else {
                return;
            }
        }
    }
}