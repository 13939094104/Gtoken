package vdsMain.model;

import android.database.Cursor;
import androidx.annotation.NonNull;

import generic.crypto.KeyCryptor;
import generic.exceptions.AccountExistException;
import generic.exceptions.NotMatchException;
import vdsMain.EncryptedHDSeed;
import vdsMain.SparseArray;
import vdsMain.db.PersonalDB;
import vdsMain.table.AddressTable;
import vdsMain.table.HDAccountTable;
import vdsMain.table.WalletTable;
import vdsMain.wallet.Wallet;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public abstract class AccountModel extends Model implements Account.AccountEvent  {

    //f13189a
    protected AccountEvent accountEvent;

    //f13190b
    private AddressTable addressTable;

    //f13191c
    private HDAccountTable hdAccountTable;

    //f13192d
    private Vector<Account> accountVector;

    //f13193e
    private SparseArray<Account> accountHashCodeToAccountsSparseArray;

    /* f13194f */
    private Vector<HDAccount> hdAccountVector;

    //f13195g
    private GeneralAccount generalAccount;

    //C3887a
    public interface AccountEvent {
        /* renamed from: a */
        void mo44065a(Account jfVar);

        /* renamed from: a */
        void mo42391a(Account jfVar, List<Address> list);

        /* renamed from: b */
        void mo42782b(Account jfVar, List<Address> list);
    }

    public AccountModel(@NonNull Wallet wallet) {
        super(wallet);
    }

    //mo43125a
    public abstract GeneralAccount getGeneralAccount(WalletTable walletTable);

    //mo44411a
    public synchronized void setAccountEvent(AccountEvent aVar) {
        this.accountEvent = aVar;
    }

    //mo44414d
    public Vector<HDAccount> getHDAccountVector() {
        return this.hdAccountVector;
    }

    //mo44413c
    public GeneralAccount getSelfGeneralAccount() {
        return this.generalAccount;
    }


    //mo44406a
    public Account getAccountFromSparseArr(int i) {
        return (Account) this.accountHashCodeToAccountsSparseArray.get(i);
    }


    @Override
    public void mo44148a(Account account, List<? extends Address> list) {
        if (getUseTransaction()) {
            account.calSubAccountBalanceInfo(false, this.wallet.getBlockChainType());
            AccountEvent aVar = this.accountEvent;
            if (aVar != null) {
                aVar.mo42391a(account, (List<Address>) list);
            }
        }
    }

    //mo44412b
    public Vector<Account> getAccountVector() {
        return this.accountVector;
    }

    //mo44408a
    public synchronized void updateHDAccountListSeed(KeyCryptor keyCryptor, String str, String str2) {
        this.hdAccountTable.beginTransaction();
        Iterator it = this.accountVector.iterator();
        while (it.hasNext()) {
            Account account = (Account) it.next();
            if (account instanceof HDAccount) {
                HDAccount hdAccount = (HDAccount) account;
                EncryptedHDSeed encyptedHDSeed = hdAccount.getEncyptedHDSeed();
                if (encyptedHDSeed != null) {
                    encyptedHDSeed.checkAndUpdateNewPwd(keyCryptor, str, str2);
                    this.hdAccountTable.updateSeedBytes(hdAccount);
                }
            }
        }
        this.hdAccountTable.endTransaction(true);
    }


    public void mo44149b(Account jfVar, List<? extends Address> list) {
        if (getUseTransaction()) {
            this.wallet.getSelfAddressModel().mo43084a((List<Address>) list, true);
            jfVar.calSubAccountBalanceInfo(false, this.wallet.getBlockChainType());
            AccountEvent aVar = this.accountEvent;
            if (aVar != null) {
                aVar.mo42782b(jfVar, (List<Address>) list);
            }
        }
    }

    public void initAllDataFromDb() {
        Cursor cursor = this.hdAccountTable.selectAll();
        this.hdAccountTable.beginTransaction();
        try {
            Account account = (Account) this.hdAccountTable.getCompleteTableItem(cursor);
            while (account != null) {
                checkTransactionAndAddAccountToCollection(account);
                account = (Account) this.hdAccountTable.getCompleteTableItem(cursor);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        cursor.close();
        this.hdAccountTable.endTransaction(false);
    }

    //mo44409a
    public void checkTransactionAndAddAccountToCollection(Account account) {
        if (getUseTransaction()) {
            this.hdAccountTable.beginTransaction();
        }
        addAccountToCollection(account);
        if (getUseTransaction()) {
            this.hdAccountTable.endTransaction(true);
        }
    }

    //mo42665a
    public void initAccountTableAndData() {
        if (this.addressTable == null) {
            PersonalDB personalDB = getPersonalDB();
            this.addressTable = personalDB.getSelfAddressTable();
            this.hdAccountTable = personalDB.getSelfHDAccountTable();
        }
        if (this.accountVector == null) {
            this.accountVector = new Vector<>();
            this.accountHashCodeToAccountsSparseArray = new SparseArray<>();
            this.hdAccountVector = new Vector<>();
        }
        this.generalAccount = getGeneralAccount((WalletTable) this.hdAccountTable);
        checkTransactionAndAddAccountToCollection((Account) this.generalAccount);
    }

    //m12809b
    private void addAccountToCollection(Account account) {
        if (!this.accountHashCodeToAccountsSparseArray.isNotNull(account.hashCode())) {
            this.accountVector.add(account);
            this.accountHashCodeToAccountsSparseArray.put(account.hashCode(), account);
            if (account instanceof HDAccount) {
                this.hdAccountVector.add((HDAccount) account);
                if (getUseTransaction()) {
                    this.hdAccountTable.replaceHDAccount(account);
                }
            }
            else if (account instanceof GeneralAccount) {
                this.generalAccount = (GeneralAccount) account;
            }
            account.setSelfAccountEvent((Account.AccountEvent) this);
            if (getUseTransaction()) {
                this.wallet.getSelfAddressModel().checkAndAddAddressToDes2AddressMap(account.getTxDesAddressList(), true, false);
                AccountEvent accountEvent = this.accountEvent;
                if (accountEvent != null) {
                    accountEvent.mo44065a(account);
                }
                account.calSubAccountBalanceInfo(true, this.wallet.getBlockChainType());
            }
        }
    }

    //mo42666f
    public void clearAccountVector() {
        this.accountVector.clear();
        this.hdAccountVector.clear();
        this.accountHashCodeToAccountsSparseArray.clear();
    }

    public synchronized void mo44410a(Account account, CharSequence charSequence) throws NotMatchException, AccountExistException {
        this.wallet.checkWalletPassword(charSequence);
        int hashCode = account.hashCode();
        if (!this.accountHashCodeToAccountsSparseArray.isNotNull(hashCode)) {
            this.wallet.lock();
            checkTransactionAndAddAccountToCollection(account);
            this.wallet.unLock();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("account ");
            sb.append(hashCode);
            sb.append(" was exists.");
            throw new AccountExistException(sb.toString());
        }
    }
}
