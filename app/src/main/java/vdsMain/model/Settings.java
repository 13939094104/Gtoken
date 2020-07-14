package vdsMain.model;

import androidx.annotation.NonNull;
import vdsMain.db.WalletDB;
import vdsMain.table.SettingsTable;
import vdsMain.wallet.Wallet;

public class Settings extends SettingsTable {

    /* renamed from: c */
    protected Wallet wallet;

    /* renamed from: d */
    private boolean firstOpen = true;

    /* access modifiers changed from: protected */
    /* renamed from: c */
    public void mo43615c(String str, String str2) {
    }

    public Settings(@NonNull WalletDB walletDB) {
        super(walletDB.getContext().getApplicationContext(), walletDB);
        this.wallet = walletDB.getWallet();
    }

    //mo43616i
    public void selectAllDataAndInit() {
        super.selectAllDataAndInit();
        this.firstOpen = getBoolValue("firstOpen", true);
    }
}