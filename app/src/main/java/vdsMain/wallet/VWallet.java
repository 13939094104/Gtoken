package vdsMain.wallet;

import android.content.Context;
import androidx.annotation.NonNull;
import com.vc.libcommon.exception.AddressFormatException;
import generic.exceptions.AddressExistsException;
import generic.exceptions.NotMatchException;
import generic.utils.AddressUtils;
import vcash.model.VTxModel;
import vcash.network.VPeerManager;
import vdsMain.BLOCK_CHAIN_TYPE;
import vdsMain.StringToolkit;
import vdsMain.VChainParam;
import vdsMain.config.WalletConfigure;
import vdsMain.db.PersonalDB;
import vdsMain.db.TransactionDB;
import vdsMain.db.VPersonalDB;
import vdsMain.db.VTransactionDB;
import vdsMain.model.*;
import vdsMain.observer.WalletObserver;
import vdsMain.peer.PeerManager;
import zcash.ZCJoinSplitParamManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//bir
public class VWallet extends Wallet {

    //f11677B
    private ZCJoinSplitParamManager zcJoinSplitParamManager;

    //f11681z
    protected VibModel vibModel = null;

    protected VIdentityModel f11678w = null;

    public VWallet(@NonNull Context context) {
        super(context);
    }

    //mo41189a
    public void addTestAddressList(ChainParams chainParams, WalletConfigure walletConfigure) {
        HashMap<String,String[]> b = walletConfigure.configFile.splitAndAddAddressToMap("test_zcash_address");
        VChainParam vChainParam = (VChainParam) chainParams;
        if (b != null && !b.isEmpty()) {
            vChainParam.setTestZcashAddressList(new ArrayList());
            for (String[] add : b.values()) {
                vChainParam.getTestZcashAddressList().add(add);
            }
        }
    }

    //mo42395ae
    public VibModel getSelfVibModel() {
        return this.vibModel;
    }

    //mo42399s
    public void initModelAndManager() {
        super.initModelAndManager();
//        this.f11679x = new bpd(this);
        this.f11678w = new VIdentityModel(this);
//        this.f11680y = new bpe(this, this.f11679x);
        this.vibModel = new VibModel(this);
//        this.f11676A = new bpc(this);
    }

    //initMainModel
    public void initMainModel() {
        super.initMainModel();
//        this.f11679x.mo42660a();
//        this.f11680y.mo42663a();
        this.vibModel.mo42699b();
//        this.f11676A.mo42625a();
        this.f11678w.mo42691c();
    }

    //mo42401x
    public void selectAllAndInitData() {
        super.selectAllAndInitData();
//        this.f11679x.selectAllAndInitData();
//        this.f11680y.selectAllAndInitData();
        this.vibModel.selectAllAndInitData();
//        this.f11676A.selectAllAndInitData();
        this.f11678w.selectAllAndInitData();
    }

    //mo42389G
    public void intiSplitParamManager() {
        if (this.zcJoinSplitParamManager == null) {
            this.zcJoinSplitParamManager = new ZCJoinSplitParamManager();
            this.zcJoinSplitParamManager.initNative(this.context, this.f13007e);
        }
    }

    public void mo42392a(ChainParams chainParams) {
        super.mo42392a(chainParams);
        VChainParam vChainParam = (VChainParam) chainParams;
        if (vChainParam.testZcashAddressList != null) {
            for (String[] strArr : vChainParam.testZcashAddressList) {
                try {
                    if (strArr.length != 1) {
                        if (strArr.length <= 1 || !StringToolkit.checkIsNull((CharSequence) strArr[1])) {
                            mo44051a(strArr[1], (CharSequence) chainParams.walletPwd);
                        }
                    }
                    AnonymousAccount i = ((VAccountModel) this.accountModel).mo42668i();
                    VAnonymousAddress vAnonymousAddress = new VAnonymousAddress(this.personalDB.getSelfAddressTable());
                    vAnonymousAddress.setFlag64(true);
                    vAnonymousAddress.mo40875a(AddressUtils.m948a((ChainParams) vChainParam, strArr[0]));
                    i.mo44139a((Address) vAnonymousAddress, true);
                } catch (AddressExistsException unused) {

                } catch (AddressFormatException e) {
                    e.printStackTrace();
                } catch (NotMatchException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    public Account mo41187a(Address address, CharSequence charSequence) throws AddressExistsException, NotMatchException {
        Account account;
        checkWalletPassword(charSequence);
        lock();
        if (address instanceof VAnonymousAddress) {
            account = ((VAccountModel) this.accountModel).mo42668i();
        } else {
            account = this.accountModel.getSelfGeneralAccount();
        }
        if (!this.addressModel.isUsingDesAddressMapHasKey(address.getCTxDestination())) {
            try {
                account.mo44139a(address, true);
                unLock();
                return account;
            } catch (Exception e) {
                throw e;
            } catch (Throwable th) {
                unLock();
                throw th;
            }
        } else {
            unLock();
            StringBuilder sb = new StringBuilder();
            sb.append("Address was already exists ");
            sb.append(address.getAddressString(new BLOCK_CHAIN_TYPE[0]));
            throw new AddressExistsException(sb.toString());
        }
    }

    public VIdentityModel mo42394ad() {
        return this.f11678w;
    }

    public BLOCK_CHAIN_TYPE getBlockChainType() {
        return BLOCK_CHAIN_TYPE.VCASH;
    }



    public ChainParams getWalletChainParams() {
        return new VChainParam(this);
    }


    public CWallet getCWallet() {
        return new VCWallet(this, this.keyCryptor);
    }


    public WalletHelper getWalletHelper() {
        return new VWalletHelper(this);
    }

    //910 版本4
    //自己修改到5
    //7 添加address 表 未压缩过的公钥字节数组 1
    public PersonalDB getPersonalDB(String prefix, String name) {
        return new VPersonalDB(this, prefix, name, 7);
    }

    public TransactionDB getTransactionDB(String str, String str2) {
        return new VTransactionDB(this, str, str2);
    }

    public TransactionModel getTransactionModel() {
        return new VTxModel(this);
    }

    public BlockChainModel getBlockChainModel() {
        return new VBlockChainModel(this);
    }

    public PeerManager getPeerManager() {
        return new VPeerManager(this);
    }

    //mo41193d
    public AddressModel getAddressModel() {
        return new VAddressModel(this);
    }

    public AccountModel getAccountModel() {
        return new VAccountModel(this);
    }

    public void mo42391a(Account jfVar, List<Address> list) {
        ((VPeerManager) this.peerManager).mo40474a(list);
        for (WalletObserver walletObserver : getWalletObserverList()) {
            if (isHasWalletObserver(walletObserver)) {
                walletObserver.mo42767a((Wallet) this, list);
            }
        }
    }

    public void clearWalletSetting(boolean z) {
        super.clearWalletSetting(z);
//        this.f11679x.mo42662b();
//        this.f11680y.mo42664b();
        this.vibModel.mo42700c();
        //this.f11676A.mo42633b();
        this.f11678w.mo42681a();
    }

}
