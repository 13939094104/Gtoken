package com.vtoken.application.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableBoolean;
import android.text.TextUtils;
import android.view.View;
import com.vtoken.application.ApplicationLoader;
import com.vtoken.application.BR;
import com.vtoken.application.R;
import com.vtoken.application.util.DateUtil;
import vdsMain.AddressType;
import vdsMain.BLOCK_CHAIN_TYPE;
import vdsMain.Constants;
import vdsMain.model.Account;
import vdsMain.tool.Util;
import vdsMain.transaction.CAmount;

import java.io.Serializable;
import java.math.BigDecimal;

public class Address extends BaseObservable implements Serializable {
    public static final int ANONYMOUS = AddressType.ANONYMOUS.mo44257a();

    /* renamed from: HD */
    public static final int f3396HD = 9;
    public static final int MONITORING = 8;
    private String address;
    private String balance;
    private String showBalance;
    private String btcAddress;
    private String parentAddress;
    private long createTimeMillions;
    private String distance;
    @Bindable
    private String inviteNumber;
    private String seasonInviteNumber;
    @Bindable
    private boolean isCheck;
    @Bindable
    protected String nickName;
    private String number;
    @Bindable
    public String percent;
    private View.OnClickListener qrCodeOnClickListener;
    private int qrCodeVisible;
    @Bindable
    public String reward;
    public ObservableBoolean showDetail = new ObservableBoolean(false);
    private int socialVisible = 8;
    @Bindable
    private String type;
    private long unLockBalance;
    @Bindable
    public String vWeight;
    private double vWeightDouble;
    private String vaddress;

    private int address_order_index;

    public boolean isMark=false;


    public boolean showRankIcon=false;

    public int rankIconResId=0;

    public Long lockTime=0L;

    private String todayPorfit="0";

    public boolean isHdAddress;

    public String getTodayPorfit(){
        return "+"+new BigDecimal(todayPorfit).toPlainString();
    }

    public void setTodayPorfit(String str){
        todayPorfit=str;
    }

    public boolean getLockStatus(){
        if(System.currentTimeMillis()/1000>lockTime){
            return false;
        }else {
            return true;
        }
    }

    public String getLockTimeFormatTip(){
        return String.format("%s计算收益",DateUtil.INSTANCE.formatTimeStampDefault(lockTime*1000,"yyyy.MM.dd HH:mm:ss"));
        //return String.format("%s计算收益",DateUtil.INSTANCE.formatTimeStampDefault(lockTime*1000,"MM月dd日HH:mm"));
    }
    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String str) {
        this.nickName = str;
        notifyPropertyChanged(32);
    }

    public String getReward() {
        return this.reward;
    }

    public void setReward(String str) {
        this.reward = str;
        notifyPropertyChanged(58);
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String str) {
        this.number = str;
    }

    public String getInviteNumber() {
        return this.inviteNumber;
    }

    public void setInviteNumber(String str) {
        this.inviteNumber = str;
        notifyPropertyChanged(17);
    }

    public String getSeasonInviteNumber() {return this.seasonInviteNumber;}

    public void setSeasonInviteNumber(String number){
        this.seasonInviteNumber=number;
    }

    public String getBalance() {
        return this.balance;
    }

    public void setBalance(String str) {
        this.balance = str;
    }

    public String getShowBalance(){
        if(showBalance==null){
            return CAmount.toDecimalSatoshiString(unLockBalance);
        }
        return this.showBalance;
    }

    public void setShowBalance(String str){
        this.showBalance =str;
    }

    public String getPercent() {
        return this.percent;
    }

    public void setPercent(String str) {
        this.percent = str;
        notifyPropertyChanged(50);
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String str) {
        this.address = str;
    }

    public String getParentAddress() {return this.parentAddress;}

    public void setParentAddress(String str){ this.parentAddress=str;}

    public String getDistance() {
        return this.distance;
    }

    public void setDistance(String str) {
        this.distance = str;
    }


    public String getCreateTimeMillString(){
        return DateUtil.INSTANCE.formatTimeStampDefault(this.createTimeMillions,"yyyy.MM.dd HH:mm:ss");
    }

    public long getCreateTimeMillions() {
        return this.createTimeMillions;
    }

    public void setCreateTimeMillions(long j) {
        this.createTimeMillions = j;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
        notifyPropertyChanged(8);
    }

    public String getvWeight() {
        return this.vWeight;
    }

    public void setvWeight(String str) {
        this.vWeight = str;
        notifyPropertyChanged(57);
    }

    public int getSocialVisible() {
        return this.socialVisible;
    }

    public void setSocialVisible(int i) {
        this.socialVisible = i;
    }

    public int getQrCodeVisible() {
        return this.qrCodeVisible;
    }

    public void setQrCodeVisible(int i) {
        this.qrCodeVisible = i;
    }

    public View.OnClickListener getQrCodeOnClickListener() {
        return this.qrCodeOnClickListener;
    }

    public void setQrCodeOnClickListener(View.OnClickListener onClickListener) {
        this.qrCodeOnClickListener = onClickListener;
    }

    public void initByRealAddress(vdsMain.model.Address address, BLOCK_CHAIN_TYPE blockChainType) {
        setType(blockChainType.name());
        this.address = address.getAddressString(blockChainType);
        this.balance = CAmount.toDecimalSatoshiString(address.getSumBalance(blockChainType));
        this.unLockBalance = address.getAvailableBalance(blockChainType);
        setNickName(address.getLabel());
        this.type = blockChainType.name();
        this.vaddress = address.getAddressString(BLOCK_CHAIN_TYPE.VCASH);
        this.btcAddress = address.getAddressString(BLOCK_CHAIN_TYPE.BITCOIN);
        this.nickName = address.getLabel();
        this.isHdAddress=false;
        this.address_order_index=address.getAddressOrderIndex();
    }

    public void initByHdRealAddress(vdsMain.model.Address address, BLOCK_CHAIN_TYPE blockChainType) {
        if (!address.isAccount()) {
            initByRealAddress(address, blockChainType);
        } else if (!address.isAccount() || !address.isFlagIndentity()) {
            setType(blockChainType.name());
            this.address = address.getAddressString(blockChainType);
            this.balance = CAmount.toDecimalSatoshiDouble(getAmount(true, address, blockChainType)).toString();
            this.unLockBalance = getAmount(false, address, blockChainType);
            setNickName(address.getLabel());
            this.type = blockChainType.name();
            this.vaddress = address.getAddressString(BLOCK_CHAIN_TYPE.VCASH);
            this.btcAddress = address.getAddressString(BLOCK_CHAIN_TYPE.BITCOIN);
        } else {
            initByRealAddress(address, blockChainType);
        }
        isHdAddress=true;
    }

    private long getAmount(boolean z, vdsMain.model.Address address, BLOCK_CHAIN_TYPE block_chain_type) {
        if (address.isAccount() && !address.isFlagIndentity()) {
            Account a = ApplicationLoader.getVcashCore().getAccount(address.getAccount());
            if (a != null) {
                return a.getSumBalance(z, block_chain_type);
            } else if (z) {
                return address.getSumBalance(block_chain_type);
            } else {
                return address.getAvailableBalance(block_chain_type);
            }
        } else if (z) {
            return address.getSumBalance(block_chain_type);
        } else {
            return address.getAvailableBalance(block_chain_type);
        }
    }

    public String getVaddress() {
        return this.vaddress;
    }

    public void setVaddress(String str) {
        this.vaddress = str;
    }

    public String getBtcAddress() {
        return this.btcAddress;
    }

    public void setBtcAddress(String str) {
        this.btcAddress = str;
    }

    public String getAddress(BLOCK_CHAIN_TYPE blockChainType) {
        switch (blockChainType) {
            case VCASH:
                return this.vaddress;
            case BITCOIN:
                return this.btcAddress;
            default:
                return this.address;
        }
    }

    public long getUnLockBalance() {
        return this.unLockBalance;
    }

    public String getUnLockBalanceString() {
        return CAmount.toDecimalSatoshiString(this.unLockBalance);
    }


    public String getBalanceUnIncludeLockedWithUnit() {
        String a = Util.m7019a(CAmount.toDecimalSatoshiDouble(Long.valueOf(this.unLockBalance)).doubleValue(), this.type);
        if (TextUtils.equals(this.type, Constants.vcashName)) {
            StringBuilder sb = new StringBuilder();
            sb.append(a);
            sb.append(" ");
            sb.append("");
            return sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(a);
        sb2.append(" ");
        sb2.append("");
        return sb2.toString();
    }

    public void setUnLockBalance(long j) {
        this.unLockBalance = j;
    }

    public double getvWeightDouble() {
        return this.vWeightDouble;
    }

    public void setvWeightDouble(double d) {
        this.vWeightDouble = d;
    }

    public boolean isCheck() {
        return this.isCheck;
    }

    public void setCheck(boolean z) {
        this.isCheck = z;
        notifyPropertyChanged(BR.isCheck);
    }

    public int getAddressOrderIndex()
    {
        return address_order_index;
    }

    public void copyVAddress(){
        Util.copyAndShowToast(this.vaddress);
    }

    public void initRankWithIndex(int index){
        number=String.valueOf(index+1);
        if(index>=0 && index<=2){
            showRankIcon=true;
            switch (index){
                case 0:
                    rankIconResId= R.drawable.icon_top1;
                    break;
                case 1:
                    rankIconResId= R.drawable.icon_top2;
                    break;
                case 2:
                    rankIconResId= R.drawable.icon_top3;
                    break;
            }
        }else{
            showRankIcon=false;
        }
    }
}