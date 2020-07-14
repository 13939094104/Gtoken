package vdsMain.model;

import androidx.annotation.NonNull;

import vdsMain.db.PersonalDB;
import vdsMain.wallet.Wallet;

public abstract class Model {

    //f13204a
    private boolean useTransaction = false;
    /* access modifiers changed from: protected */

    //f13205h
    //910 f13344i
    //915 f13875i
    public Wallet wallet;

    //910 mo40487g
    //mo40459g
    public abstract void initAllDataFromDb();

    public Model(@NonNull Wallet izVar) {
        this.wallet = izVar;
    }

    //mo44469o
    public Wallet getWalllet() {
        return this.wallet;
    }

    //mo44470p
    public PersonalDB getPersonalDB() {
        return this.wallet.getPersonalDB();
    }

    //mo44471q
    public boolean getUseTransaction() {
        return this.useTransaction;
    }

    //mo44472r
    public final void selectAllAndInitData() {
        initAllDataFromDb();
        this.useTransaction = true;
    }
}