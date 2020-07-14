package com.vtoken.application.viewModel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import bitcoin.CNoDestination;
import bitcoin.account.hd.HDSeed;
import com.google.zxing.integration.android.IntentIntegrator;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;
import com.vc.libcommon.exception.TxSizeException;
import com.vtoken.vdsecology.vcash.VCashCore;
import com.vtoken.application.ApplicationLoader;
import com.vtoken.application.BR;
import com.vtoken.application.R;
import com.vtoken.application.databinding.DialogMakeCollectionsBinding;
import com.vtoken.application.databinding.DialogNoteChangeBinding;
import com.vtoken.application.databinding.DialogPromptWithModelBinding;
import com.vtoken.application.databinding.DialogWalletConfirmTransactionBinding;
import com.vtoken.application.util.DrawViewUtil;
import com.vtoken.application.util.WalletUtil;
import com.vtoken.application.view.activity.BaseActivity;
import com.vtoken.application.view.activity.ScanActivity;
import com.vtoken.application.view.activity.wallet.PrivateKeyTypeActivity;
import com.vtoken.application.view.activity.wallet.WalletAddressCreateActivity;
import com.vtoken.application.view.activity.wallet.WalletMainActivity;
import com.vtoken.application.view.fragment.BaseFragment;
import com.vtoken.application.widget.dialog.FragmentDialog;


import generic.exceptions.InvalidateTransactionException;
import generic.exceptions.UtxoAlreadySpendException;
import generic.exceptions.UtxoNotFoundException;
import generic.utils.AddressUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import vdsMain.*;
import vdsMain.model.Address;
import vdsMain.model.HDAccount;
import vdsMain.model.VBlockChainModel;
import vdsMain.tool.DeviceUtil;
import vdsMain.tool.SharedPreferencesUtil;
import vdsMain.tool.Util;
import vdsMain.transaction.*;
import vdsMain.wallet.Wallet;
import vdsMain.wallet.WalletType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static vdsMain.BLOCK_CHAIN_TYPE.BITCOIN;

public class BaseViewModel {
    public  static  String ZLY="自来也";
    public static String[] coinTypeArr = {"USD", "EUR", "GBP", "JPY", "CNY", "CAD", "AUD", "RUB", "PKR", "KRW", "CHF", "BTC"};

    //f9467m
    public static HDSeed sHDSeed;

    //f9472e
    //915 f9914e
    public Context context;

    //f9483q
    //915 f9925q
    public Handler mainHandler;

    //f9479l
    //915 f9921l
    //917 f9943l
    public VCashCore vCashCore;

    public ItemTouchHelper f9475h;
    protected int classGuid;
    //f9470c
    public PopupWindow popupWindow;

    //f9471d
    protected RecyclerView recyclerView;

    //f9473f
    protected Fragment fragment;

    //f9476i
    protected int scanRequestCode;

    private int loadingReferenceNumber=0;

    protected Dialog visibleDialog;

    //f9490x
    private FragmentDialog loadingDialog;

    //f9469b
    public FragmentDialog confirmDialog;

    //f9492z
    public FragmentDialog qrOptionDialog;

    //f9487u
    public ObservableBoolean isColdWallet = new ObservableBoolean();

    //0 没有节点 8 有节点
    public ObservableField<Integer> f9488v = new ObservableField<>(8);


    protected boolean inPreviewMode;

    //915 mo41590h
    public Context getContext() {
        return this.context;
    }

    //f9480n
    public BLOCK_CHAIN_TYPE block_chain_type = ApplicationLoader.getBlockChainType();

    public BaseViewModel(Context context) {
        this.context = context;
        Init();
    }

    public BaseViewModel(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        Init();
    }



    //mo41564b
    public void startService(Intent intent) {
        this.context.startService(intent);
    }

    public Activity getParentActivity()  {
        return (Activity) this.context;
    }

    //mo41396i
    public Activity getActivity() {
        return (Activity) this.context;
    }

    //mo41325a
    //915 mo41518a
    public void startActivity(Intent intent) {
        this.context.startActivity(intent);
    }

    //mo41326a
    //915 mo41519a
    public void startActivityForResult(Intent intent, int i) {
        ((Activity) this.context).startActivityForResult(intent, i);
    }

    //mo40017a
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    //mo41393h
    //915 mo41592i
    public void finish() {
        ((Activity) this.context).finish();
    }

    public void onResume(){

    }

    //C3215a
    public interface QrOptionDialogEvent {
        //mo41441a
        void onCopyClick();

        //mo41442b
        void onSaveClick();

        /* renamed from: c */
        void mo41443c();
    }

    //mo39924a
    private void Init() {
        this.vCashCore = ApplicationLoader.getVcashCore();
        if (!mo41316L()) {
            this.mainHandler = new Handler(Looper.getMainLooper());
        }
        if (this.vCashCore.getWalletType() == WalletType.COOL) {
            this.isColdWallet.set(true);
        } else {
            this.isColdWallet.set(false);
        }
    }

    public boolean mo41316L() {
        return false;
    }

    //mo41310F
    //915 mo41502G
    public final <T> LifecycleTransformer<T> bindDestoryEvent() {
        Context context = this.context;
        if (context instanceof RxActivity) {
            return ((RxActivity) context).bindUntilEvent(ActivityEvent.DESTROY);
        }

        return ((RxActivity) ApplicationLoader.sActivity).bindUntilEvent(ActivityEvent.DESTROY);
    }


    //mo41317M
    public void setPeerStatus() {
        switch (ApplicationLoader.getBlockChainType()) {
            case VCASH:
                if (!ApplicationLoader.getSingleApplicationContext().hasVcashPeer()) {
                    this.f9488v.set(0);
                    return;
                } else {
                    this.f9488v.set(8);
                    return;
                }
            default:
                return;
        }
    }

    public List<com.vtoken.application.model.Address> getVxdViewAddressList(boolean skipCheckBalance){
        return getVxdViewAddressList(skipCheckBalance,false);
    }

    public List<com.vtoken.application.model.Address> getVxdViewAddressList(boolean skipCheckBalance, boolean skipCheckVxd){
        ArrayList<com.vtoken.application.model.Address> viewAddressList= new ArrayList<>();
        List<Address> generalAddressList = this.vCashCore.getAddressListByFilter(AddressFilter.GENERAL);
        if (generalAddressList != null && !generalAddressList.isEmpty()) {
            for (Address generalAddress : generalAddressList) {
                if ((skipCheckVxd||generalAddress.isAppingVxd()) && (skipCheckBalance||CAmount.toDecimalSatoshiDouble(generalAddress.getAvailableBalance(this.block_chain_type))> 0.0)) {
                    com.vtoken.application.model.Address address =new com.vtoken.application.model.Address();
                    address.initByRealAddress(generalAddress, BLOCK_CHAIN_TYPE.VCASH);
                    viewAddressList.add(address);
                }
            }
        }
        List<Address> indentityAddressList = this.vCashCore.getAddressListByFilter(AddressFilter.INDENTITY);
        if (indentityAddressList != null && !indentityAddressList.isEmpty()) {
            for (Address indentityAddress : indentityAddressList) {
                if ((skipCheckVxd||indentityAddress.isAppingVxd()) && (skipCheckBalance||CAmount.toDecimalSatoshiDouble(indentityAddress.getAvailableBalance(this.block_chain_type))> 0.0)) {
                    com.vtoken.application.model.Address address =new com.vtoken.application.model.Address();
                    address.initByRealAddress(indentityAddress, BLOCK_CHAIN_TYPE.VCASH);
                    viewAddressList.add(address);
                }
            }
        }
        List<HDAccount> hdAccountList = this.vCashCore.getHDAccountList();
        if (hdAccountList != null && !hdAccountList.isEmpty()) {
            HDAccount hdAccount = hdAccountList.get(0);
            Address address = hdAccount.getAddrAddress();
            if (address != null &&(skipCheckVxd||address.isAppingVxd())  && (skipCheckBalance||CAmount.toDecimalSatoshiDouble(address.getAvailableBalance(this.block_chain_type))> 0.0)) {
                com.vtoken.application.model.Address address2 = new com.vtoken.application.model.Address();
                address2.initByRealAddress(address, BLOCK_CHAIN_TYPE.VCASH);
                viewAddressList.add(address2);
            }
        }
        return viewAddressList;
    }

    public String initTransferInTransaction(String pwd,Double d_transferAmount,String address,String toAddress,Boolean includeFee){
        return initTransferInTransaction(pwd,d_transferAmount,address,toAddress,includeFee,0.0001);
    }

    public String initTransferInTransaction(String pwd,Double d_transferAmount,String address,String toAddress,Boolean includeFee,double fee){
        Address  realAddress = vCashCore.getAddressFromAddressString(address, BLOCK_CHAIN_TYPE.VCASH);
        List<Utxo> utxoList = vCashCore.getUtxoListByAddress(realAddress, BLOCK_CHAIN_TYPE.VCASH);
        List<Utxo> spendUtxoList = vCashCore.getSpendUtxoList(includeFee, utxoList, CAmount.toSatoshiLong(d_transferAmount), CAmount.toSatoshiLong(fee));
        ArrayList<AddressMoneyInfo> addressMoneyInfos=new ArrayList<AddressMoneyInfo>();
        addressMoneyInfos.add(new AddressMoneyInfo(toAddress,CAmount.toSatoshiLong(d_transferAmount),0));
        try {
            if(includeFee){
                d_transferAmount-=fee;
            }
            Transaction transaction=vCashCore.createVxdTransferInTransaction(
                    utxoList,
                    spendUtxoList,
                    addressMoneyInfos ,
                    realAddress,
                    pwd,
                    BLOCK_CHAIN_TYPE.VCASH,
                    includeFee
            );
            return transaction.getTransactionHexString();
            //LocalBroadcastManager.getInstance(this@CreateVxdViewModel.context.getApplicationContext()).sendBroadcast(Intent("refresh_vxd"))
        } catch (InvalidateTransactionException e) {
            dismissLoadingAndShowToast(getStringRescourcesByResName("toast_send_amount_error"));
            e.printStackTrace();
        } catch (UtxoAlreadySpendException e) {
            e.printStackTrace();
            dismissLoadingAndShowToast(getStringRescourcesByResName("toast_utxo_is_pay"));
        } catch (UtxoNotFoundException e) {
            e.printStackTrace();
            dismissLoadingAndShowToast(getStringRescourcesByResName("toast_utxo_is_pay"));
        } catch (TxSizeException e) {
            e.printStackTrace();
            dismissLoadingAndShowToast(getStringRescourcesByResName("toast_utxo_too_many"));
        } catch ( Exception e) {
            e.printStackTrace();
            dismissLoadingAndShowToast(getStringRescourcesByResName("unknow_error"));
        }
        return null;
    }

    public Dialog showDialog(Dialog dialog) {
        return showDialog(dialog, false, null);
    }

    public Dialog showDialog(Dialog dialog, boolean allowInTransition, final Dialog.OnDismissListener onDismissListener) {
        if (dialog == null) {
            return null;
        }
        try {
            if (visibleDialog != null) {
                visibleDialog.dismiss();
                visibleDialog = null;
            }
        } catch (Exception e) {
            //FileLog.e(e);
        }
        try {
            visibleDialog = dialog;
            visibleDialog.setCanceledOnTouchOutside(true);
            visibleDialog.setOnDismissListener(dialog1 -> {
                if (onDismissListener != null) {
                    onDismissListener.onDismiss(dialog1);
                }
                onDialogDismiss(visibleDialog);
                visibleDialog = null;
            });
            visibleDialog.show();
            return visibleDialog;
        } catch (Exception e) {
            //FileLog.e(e);
        }
        return null;
    }

    protected void onDialogDismiss(Dialog dialog) {

    }

    public void clearLoadingDialog() {
        FragmentDialog dialog = this.loadingDialog;
        if (!(dialog == null || dialog.getView() == null)) {
            this.loadingDialog.dismiss();
        }
        this.loadingDialog = null;
    }

    //mo41400k
    //915 mo41599l
    public void dismissConfirmDialog() {
        FragmentDialog warnDialog = this.confirmDialog;
        if (warnDialog != null && warnDialog.getView() != null) {
            this.confirmDialog.dismiss();
        }
    }

    //mo41397i
    public void onAddressLabelConfirm(String str) {

    }

    //mo41879r
    //915 mo41570c
    public void dismissLoadingAndShowToast(String str) {
        this.mainHandler.post(new Runnable() {
            @Override
            public void run() {
                BaseViewModel.this.dismissLoadingDialog();
                BaseViewModel.this.showToast(str);
            }
        });
    }

    //mo41398j
    public void startRecyclerAnimation() {
        Fragment fragment = this.fragment;
        if (fragment != null) {
            ((BaseFragment) fragment).mo41262b();
        } else {
            ((BaseActivity) this.context).startRecyclerAnimation();
        }
    }

    //mo39910B
    public void removeHandlerAndClearDialog() {
        Handler handler = this.mainHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        dismissConfirmDialog();
        clearLoadingDialog();
    }

    //mo41350a
    //915 mo41546a
    public void showConfirmDialog(String str, boolean cancelable) {
        FragmentDialog confirmDialog = this.confirmDialog;
        if (confirmDialog != null && !confirmDialog.isVisible() && !this.confirmDialog.isAdded()) {
            this.confirmDialog.show(((Activity) this.context).getFragmentManager(), str);
            this.confirmDialog.setCancelable(cancelable);
        }
    }

    //915 mo41544a
    public void showNorConfirmDialog(String title, String info, String leftBtn, String rightBtn, @NonNull View.OnClickListener leftBtnClickEvent, @NonNull View.OnClickListener rightBtnClickEvent) {
        View inflate = LayoutInflater.from(ApplicationLoader.getSingleApplicationContext()).inflate(R.layout.dialog_prompt_with_model, null, false);
        ((TextView) inflate.findViewById(R.id.title)).setText(title);
        ((TextView) inflate.findViewById(R.id.info_text)).setText(info);
        TextView textView = (TextView) inflate.findViewById(R.id.left_text);
        textView.setText(leftBtn);
        TextView textView2 = (TextView) inflate.findViewById(R.id.right_text);
        textView2.setText(rightBtn);
        textView.setOnClickListener(leftBtnClickEvent);
        textView2.setOnClickListener(rightBtnClickEvent);
        setConfirmDialogView(inflate);
        showConfirmDialog("showNorDialog", false);
    }

    //915 mo41568c
    //mo41372c
    public String getString(int i) {
        return this.context.getString(i);
    }



    public void showQrCodeDialog(String qrCodeContent,String title, String info, boolean showOption, String content, QrOptionDialogEvent qrOptionDialogEvent) {
        showQrCodeDialog(qrCodeContent,title,info,showOption,content,qrOptionDialogEvent,-1);
    }


    //mo41349a
    public void showQrCodeDialog(String qrCodeContent,String title, String info, boolean showOption, String content, QrOptionDialogEvent qrOptionDialogEvent,int headIconId) {
        this.qrOptionDialog = null;
        final DialogMakeCollectionsBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(ApplicationLoader.getSingleApplicationContext()), R.layout.dialog_make_collections, null, false);
        //final DialogQrWithOptionBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(ApplicationLoader.getSingleApplicationContext()), R.layout.dialog_qr_with_option, null, false);
        Observer<Bitmap> observer = new Observer<Bitmap>() {
            public void onComplete() {
            }

            public void onSubscribe(Disposable disposable) {
            }

            public void onError(Throwable th) {
                th.printStackTrace();
            }

            /* renamed from: a */
            public void onNext(Bitmap bitmap) {
                dataBinding.ivQr.setImageBitmap(bitmap);
            }
        };
        dataBinding.title.setText(title);
        if(headIconId==-1){
            dataBinding.headicon.setImageResource(R.drawable.icon_vds);
        }else {
            dataBinding.headicon.setImageResource(headIconId);
        }
        int centerResourceId = R.drawable.loading_bg;
        Util.getQrCodeBitmapWithCenterDrawable(qrCodeContent, observer, -1);
        if (!TextUtils.isEmpty(info)) {
            dataBinding.tvInfo.setVisibility(View.VISIBLE);
            dataBinding.tvInfo.setText(info);
        }
        //dataBinding.ivShare.setVisibility(View.INVISIBLE);
        if (showOption) {
            dataBinding.options.setVisibility(View.VISIBLE);
            dataBinding.tvCode.setText(qrCodeContent);
            dataBinding.tvCode.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View view) {
                    Util.copyAndShowToast(qrCodeContent);
                    return false;
                }
            });
            dataBinding.ivCopy.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Util.copyAndShowToast(qrCodeContent);
                    if (qrOptionDialogEvent != null) {
                        qrOptionDialogEvent.onCopyClick();
                    }
                }
            });
            View imageView = dataBinding.ivSave;
            View.OnClickListener saveClick = new View.OnClickListener() {
                public void onClick(View view) {
                    BaseViewModel.this.saveBitmapToLocal(DrawViewUtil.getBitmapFromView((dataBinding).contentLayout), TextUtils.isEmpty(content) ? qrCodeContent : content);
                    if (qrOptionDialogEvent != null) {
                        qrOptionDialogEvent.onSaveClick();
                    }
                }
            };
            imageView.setOnClickListener(saveClick);
        } else {
            dataBinding.options.setVisibility(View.GONE);
        }
        this.qrOptionDialog = new FragmentDialog(false, false);
        this.qrOptionDialog.setView(dataBinding.getRoot());
        if (!this.qrOptionDialog.isVisible()) {
            this.qrOptionDialog.show(((Activity) this.context).getFragmentManager(), "QrWithOption");
            this.qrOptionDialog.setCancelable(true);
        }
    }

    public void mo41313I() {
        FragmentDialog bbz = this.qrOptionDialog;
        if (bbz != null && bbz.isVisible()) {
            this.qrOptionDialog.dismiss();
        }
    }

    public void saveBitmapToLocal(final Bitmap bitmap, final String str){
        saveBitmapToLocal(bitmap,str,true);
    }

    //mo41328a
    public void saveBitmapToLocal(final Bitmap bitmap, final String str,boolean isOpen) {
        Observable.create(new ObservableOnSubscribe<File>() {
            public void subscribe(ObservableEmitter<File> observableEmitter) throws IOException {
                StringBuilder sb;
                File file = new File(Constants.imageDir);
                if (!file.exists()) {
                    file.mkdirs();
                }
                String absolutePath = file.getAbsolutePath();
                if (TextUtils.isEmpty(str)) {
                    sb = new StringBuilder();
                    sb.append(System.currentTimeMillis());
                } else {
                    sb = new StringBuilder();
                    sb.append(str);
                }
                sb.append(".png");
                File file2 = new File(absolutePath, sb.toString());
                file2.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                observableEmitter.onNext(file2);
            }
        }).subscribeOn(Schedulers.io()).compose(bindDestoryEvent()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<File>() {
            public void onComplete() {
            }

            public void onSubscribe(Disposable disposable) {
            }

            /* renamed from: a */
            public void onNext(File file) {
                new MediaScanner(ApplicationLoader.getSingleApplicationContext(), file);
                if(isOpen){
                    BaseViewModel.this.showSaveFileDialog(file, isOpen);
                }else {
                    vdsMain.tool.FileUtils.startFileSendIntent(file.getPath(), BaseViewModel.this.context);
                }
            }

            public void onError(Throwable th) {
                th.printStackTrace();
            }
        });
    }


    public void onDestroy(){

    }


    //mo41337a
    public void showSaveFileDialog(final File file, final boolean isOpen) {
        if (this.confirmDialog != null) {
            dismissConfirmDialog();
            this.confirmDialog = null;
        }
        PromptDialogViewModel viewModel = new PromptDialogViewModel(this.context);
        viewModel.setConfirmString(getStringRescourcesByResName("open_dir"));
        viewModel.setCancelString(getStringRescourcesByResName("close"));
        if (true) {
            StringBuilder sb = new StringBuilder();
            sb.append(getStringRescourcesByResName("save_file_location"));
            sb.append("<font color=\"#ffffff\"><br/>");
            sb.append(file.getPath());
            sb.append("</font>");
            viewModel.setSpanned(Html.fromHtml(sb.toString()));
        } else if (this.block_chain_type == BITCOIN) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getStringRescourcesByResName("save_file_location"));
            sb2.append("<font color=\"#7B8AA4\"><br/>");
            sb2.append(file.getPath());
            sb2.append("</font>");
            viewModel.setSpanned(Html.fromHtml(sb2.toString()));
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(getStringRescourcesByResName("save_file_location"));
            sb3.append("<font color=\"#ffffff\"><br/>");
            sb3.append(file.getPath());
            sb3.append("</font>");
            viewModel.setSpanned(Html.fromHtml(sb3.toString()));
        }
        createConfirmView(viewModel, (View.OnClickListener) new View.OnClickListener() {
            public void onClick(View view) {
                BaseViewModel.this.dismissConfirmDialog();
                if (isOpen) {
                    vdsMain.tool.FileUtils.startFileIntent(file.getPath(), BaseViewModel.this.context);
                } else {
                    vdsMain.tool.FileUtils.startFileSendIntent(file.getPath(), BaseViewModel.this.context);
                    //BaseViewModel.this.jumpFileBrowserActivity(file);
                }
            }
        }, (View.OnClickListener) new View.OnClickListener() {
            public void onClick(View view) {
                BaseViewModel.this.dismissConfirmDialog();
            }
        });
        showConfirmDialog("saveFileDialog", true);
    }

    //mo41336a
    public void jumpFileBrowserActivity(File file) {
//        Intent intent = new Intent(this.context, FileBrowser.class);
//        intent.putExtra("path", file.getAbsolutePath());
//        startActivity(intent);
    }

    //mo41385e
    public boolean isStringEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    //mo41364b
    //915 mo41560b
    public String getStringRescourcesByResName(String str) {
        return ApplicationLoader.getSingleApplicationContext().getStringFromLocal(str);
    }

    public Drawable getDrawable(int resourcesId){
       return context.getResources().getDrawable(resourcesId);
    }

    //mo41306A
    //915 mo41498B
    public void dismissLoadingDialog() {
        FragmentDialog fragmentDialog = this.loadingDialog;
        if (!(fragmentDialog == null || fragmentDialog.getView() == null)) {
            this.loadingDialog.dismiss();
        }
        this.loadingDialog = null;
    }

    //mo41389f
    public boolean checkWalletPwdFormat(String str) {
        return Pattern.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,43}$", str);
    }

    public boolean checkRealNameFormat(String str){
        if(isStringEmpty(str)){
            return false;
        }
        return Pattern.matches("^[\\u4E00-\\u9FA5]{2,10}$",str);
    }

    public boolean checkSimpleMobileNumberFormat(String str){
        if(isStringEmpty(str)){
            return false;
        }
        return Pattern.matches("^\\d{11}$",str);
    }

    public boolean checkSimpleEthAddressFormat(String str){
        if(isStringEmpty(str)){
            return false;
        }
        return Pattern.matches("^0x\\w{40}$",str);
    }

    public boolean checkSimpleIdCardFormat(String str){
        if(isStringEmpty(str)){
            return false;
        }
        return Pattern.matches("^\\d{17}[0-9X]$",str);
    }

    public boolean checkSimpleCreditCardFormat(String str){
        if(isStringEmpty(str)){
            return false;
        }
        return Pattern.matches("^\\d{16}$",str);
    }

    public boolean checkSimpleBankCardFormat(String str){
        if(isStringEmpty(str)){
            return false;
        }
        return Pattern.matches("^\\d{16}|\\d{19}$",str);
    }

    public boolean checkNumberFormat(String str,int length){
        if(isStringEmpty(str)){
            return false;
        }
        return Pattern.matches("^\\d{"+ length +"}$",str);
    }

    public boolean checkNumberFormatRange(String str,int min,int max){
        if(isStringEmpty(str)){
            return false;
        }
        String maxString="";
        if(max<0)
            maxString="";
        else
            maxString=String.valueOf(max);
        return Pattern.matches("^\\d{"+ min +","+maxString+"}$",str);
    }

    public boolean checkEnglishWordFormatRange(String str,int min,int max){
        if(isStringEmpty(str)){
            return false;
        }
        String maxString="";
        if(max<0)
            maxString="";
        else
            maxString=String.valueOf(max);
        return Pattern.matches("^[a-zA-z]{"+ min +","+maxString+"}$",str);
    }

    //mo41386f
    //915 mo41586g
    public Intent getIntent() {
        return ((Activity) this.context).getIntent();
    }

    //mo41382e
    public LayoutInflater getLayoutInflater() {
        return ((Activity) this.context).getLayoutInflater();
    }

    //mo41414s
    public View getPopupListView() {
        View inflate = getLayoutInflater().inflate(R.layout.pop_up_list, null, false);
        this.popupWindow = new PopupWindow(inflate, -2, -2, true);
        return inflate;
    }

    //mo41379d
    public void showPopupAsDropDown(View view) {
        this.popupWindow.showAsDropDown(view, DeviceUtil.dp2px(this.context, 0.0f), DeviceUtil.dp2px(this.context, 5.0f), 48);
    }

    //mo41371c
    public PopupWindow setWidthAndShowWindow(View view) {
        this.popupWindow.setWidth(view.getWidth());
        showRecyclerViewPopupWindow(view);
        return this.popupWindow;
    }

    //mo41369b
    public void showRecyclerViewPopupWindow(View view) {
        this.recyclerView.scheduleLayoutAnimation();
        this.popupWindow.showAsDropDown(view, 0, DeviceUtil.dp2px(this.context, 1.0f), 80);
    }

    //mo41373c
    //915 mo41570c
    public void showToast(String str) {
        dismissLoadingDialog();
        Toast makeText = Toast.makeText(this.context, str, Toast.LENGTH_SHORT);
        makeText.setGravity(17, 0, 0);
        makeText.show();
    }

    //mo41411q
    public void initiateScan() {
        new IntentIntegrator((Activity) this.context).setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES).setOrientationLocked(false).setBeepEnabled(true).setCaptureActivity(ScanActivity.class).initiateScan();
    }


    public void mo41353a(Map<String, String> map) {
        IntentIntegrator intentIntegrator = new IntentIntegrator((Activity) this.context);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES).setOrientationLocked(false).setBeepEnabled(true).setCaptureActivity(ScanActivity.class);
        for (Map.Entry entry : map.entrySet()) {
            intentIntegrator.addExtra((String) entry.getKey(), entry.getValue());
        }
        intentIntegrator.initiateScan();
    }

    //C3216b
    public interface conFirmDialogEvent {
        //mo41481a
        void onConfirm();

        //mo41482b
        void onCancel();
    }

    //mo41387f
    public void executeIntent(int i) {
        if (i != 1) {
            switch (i) {
                case 2:
                    Intent intent = new Intent(this.context, PrivateKeyTypeActivity.class).putExtra("direction",0);
                    startActivity(intent);
                    break;
                case 3:
                    startActivity(new Intent(this.context, WalletAddressCreateActivity.class));
                    break;
                default:
                    return;
            }
        } else {
//            Intent intent2 = new Intent(this.context, CreateAddressActivity.class);
//            startActivity(intent2);
        }
        dismissPopupWindow();
    }

    //mo41402l
    public void dismissPopupWindow() {
        PopupWindow popupWindow = this.popupWindow;
        if (popupWindow != null && popupWindow.getContentView() != null && this.popupWindow.isShowing()) {
            this.popupWindow.dismiss();
        }
    }

    //mo41413r
    public void initPopListLayout() {
        View s = getPopupListView();
        LinearLayout linearLayout = (LinearLayout) s.findViewById(R.id.pop_list_layout);
        View walletImportKey = getTextView(linearLayout, getStringRescourcesByResName("wallet_import_key"), false,R.drawable.icon_import_privkey);
        walletImportKey.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BaseViewModel.this.executeIntent(2);
            }
        });
        Log.d("自来也", getStringRescourcesByResName("wallet_craete_address") );
        View walletCreateAddress = getTextView(linearLayout, getStringRescourcesByResName("wallet_craete_address"), false,R.drawable.icon_add_address);
        walletCreateAddress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BaseViewModel.this.executeIntent(3);
            }
        });
    }

    @SuppressLint({"ResourceType"})
    //mo41318a
    public View getTextView(LinearLayout linearLayout, String str, boolean addDivider,int resId) {
        LinearLayout horizonLayout=new LinearLayout(this.context);
        horizonLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams horizonParams = new LinearLayout.LayoutParams(DeviceUtil.dp2px(this.context,148),DeviceUtil.dp2px(this.context,48));
        horizonLayout.setLayoutParams(horizonParams);
        //horizonLayout.setBackground(this.context.getDrawable(R.drawable.dialog_text_selector_v));
        TextView textView = new TextView(this.context);
        int padding = DeviceUtil.dp2px(this.context, 8);
        textView.setPadding(padding, 0, 0, 0);
        textView.setText(str);
        textView.setTextSize(15.0f);
        textView.setGravity(Gravity.LEFT);
        textView.setSingleLine();
        textView.getPaint().setFakeBoldText(true);
        textView.setTextColor(this.context.getColor(R.color.mainblackblock));
        //textView.setTextColor(ContextCompat.getColorStateList(this.context, R.drawable.dialog_text_selector_v));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        textView.setLayoutParams(layoutParams);
        if(resId>0){
            //图片
            ImageView icon=new ImageView(this.context);
            icon.setImageResource(resId);
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(DeviceUtil.dp2px(this.context,24), DeviceUtil.dp2px(this.context,24));
            iconParams.gravity=Gravity.CENTER_VERTICAL;
            iconParams.leftMargin=DeviceUtil.dp2px(this.context,16);
            icon.setLayoutParams(iconParams);
            horizonLayout.addView(icon);
        }
        horizonLayout.addView(textView);
        linearLayout.addView(horizonLayout);
        if (addDivider) {
            View view = new View(this.context);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, DeviceUtil.dp2px(this.context, 0.5f));
            layoutParams2.leftMargin = DeviceUtil.dp2px(this.context, 10.0f);
            layoutParams2.rightMargin = DeviceUtil.dp2px(this.context, 10.0f);
            view.setBackgroundColor(ContextCompat.getColor(this.context, R.color.pop_up_divider));
            view.setLayoutParams(layoutParams2);
            linearLayout.addView(view);
        }
        return horizonLayout;
    }

    @SuppressLint({"ResourceType"})
    //mo41318a
    public View getTextView(LinearLayout linearLayout, String str, boolean addDivider) {
        return getTextView(linearLayout,str,addDivider,-1);
    }

    //mo41378d
    public String mergeStingListWithBlank(List<String> list) {
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append((String) list.get(i));
            sb.append(" ");
            str = sb.toString();
        }
        return str;
    }

    //mo41352a
    public void showLabelChangeConfirmDialog(final String str, boolean z, int... iArr) {
        final DialogNoteChangeBinding noteChangeBinding = (DialogNoteChangeBinding) DataBindingUtil.inflate(LayoutInflater.from(ApplicationLoader.getSingleApplicationContext()), R.layout.dialog_note_change, null, false);
        noteChangeBinding.setVariable(42, this);
        setConfirmDialogView(noteChangeBinding.getRoot());
        showConfirmDialog("ReviseMark", true);
        EditText editText = noteChangeBinding.editText;
        InputFilter[] inputFilterArr = new InputFilter[1];
        inputFilterArr[0] = new InputFilter.LengthFilter((iArr == null || iArr.length < 1) ? 24 : iArr[0]);
        editText.setFilters(inputFilterArr);
        noteChangeBinding.editText.setText(str);
        noteChangeBinding.cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                noteChangeBinding.editText.setText(str);
                BaseViewModel.this.dismissConfirmDialog();
            }
        });
        noteChangeBinding.confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BaseViewModel.this.onAddressLabelConfirm(noteChangeBinding.editText.getText().toString());
                BaseViewModel.this.dismissConfirmDialog();
            }
        });
    }

    //mo41354a
    public void showSendTxConfirmDialog(TxInfo txInfo, BLOCK_CHAIN_TYPE igVar, final conFirmDialogEvent conFirmDialogEvent) {
        DialogWalletConfirmTransactionBinding txConfirmBinding =DataBindingUtil.inflate(LayoutInflater.from(ApplicationLoader.getSingleApplicationContext()), R.layout.dialog_wallet_confirm_transaction, null, false);
        txConfirmBinding.setVariable(BR.txInfo,txInfo);
        setConfirmDialogView(txConfirmBinding.getRoot());
        //this.confirmDialog.getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        showConfirmDialog("sendTxAsk", true);
        txConfirmBinding.back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BaseViewModel.this.dismissConfirmDialog();
                conFirmDialogEvent.onCancel();
            }
        });
        txConfirmBinding.confirmBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BaseViewModel.this.dismissConfirmDialog();
                conFirmDialogEvent.onConfirm();
            }
        });
    }

    //mo41417v
    //915 mo41615w
    @SuppressLint("WrongConstant")
    public void jumpToWalletMainAndRemoveAll() {
        Intent intent = new Intent(this.context, WalletMainActivity.class);
        intent.addFlags(32768);
        startActivity(intent);
        dismissLoadingDialog();
        finish();
        ActivityManager.getInstance().removeAll();
    }

    public void showLoadingFragmentReference(CharSequence tipString) {
        if(loadingReferenceNumber>0)
            loadingReferenceNumber++;
        else{
            loadingReferenceNumber++;
            showLoadingFragment(tipString);
        }
    }

    public void dismissLoadingFragmentReference() {
        loadingReferenceNumber--;
        if(loadingReferenceNumber<=0){
            dismissLoadingDialog();
        }
    }

    //mo41338a
    //915 mo41533a
    public void showLoadingFragment(CharSequence tipString) {
        View inflate = LayoutInflater.from(ApplicationLoader.getSingleApplicationContext()).inflate(R.layout.dialog_loading, null, false);
        ((TextView) inflate.findViewById(R.id.msg_text)).setText(tipString);
        if (this.loadingDialog == null) {
            this.loadingDialog = new FragmentDialog();
        }
        this.loadingDialog.setView(inflate);
        FragmentDialog fragmentDialog = this.loadingDialog;
        if (fragmentDialog != null&&!fragmentDialog.isAdded() && !fragmentDialog.isVisible()) {
            this.loadingDialog.show(((Activity) this.context).getFragmentManager(), "loadingMsg");
            this.loadingDialog.setCancelable(false);
        }
    }

    //mo41330a
    //915 mo41524a
    public void setConfirmDialogView(View view) {
        if (this.confirmDialog == null) {
            this.confirmDialog = new FragmentDialog();
        }
        this.confirmDialog.setView(view);
    }

    public void setConfirmDialogViewBottom(View view) {
        if (this.confirmDialog == null) {
            this.confirmDialog = new FragmentDialog();
        }
        this.confirmDialog.setView(view);
        this.confirmDialog.gravity=Gravity.BOTTOM;
    }
    //mo41335a
    //915 mo41529a
    public void createConfirmView(@NonNull PromptDialogViewModel promptDialogViewModel, @NonNull View.OnClickListener onClickListener, @NonNull View.OnClickListener onClickListener2) {
        DialogPromptWithModelBinding dataBinding= DataBindingUtil.inflate(LayoutInflater.from(ApplicationLoader.getSingleApplicationContext()), R.layout.dialog_prompt_with_model, null, false);
        dataBinding.leftText.setOnClickListener(onClickListener);
        dataBinding.rightText.setOnClickListener(onClickListener2);
        dataBinding.setVariable(BR.promptDialogModel,promptDialogViewModel);
        setConfirmDialogView(dataBinding.getRoot());
    }

    public Double getTransactionBalanceByAddress(Transaction transaction, Address address) {
        long value;
        if (!address.isAccount() || address.isFlagIndentity()) {
            value = transaction.getAddressVoutSumSubVinSum(address);
        } else {
            value = WalletUtil.m7076g(transaction);
        }
        return CAmount.toDecimalSatoshiDouble(Long.valueOf(value));
    }

    //mo41320a
    public String getTransactionBalanceStringByAddress(Transaction transaction, Address address) {
        long j;
        StringBuilder sb = new StringBuilder();
        sb.append("getBalance: ");
        sb.append(transaction.getTxId());
        Log.d("BaseViewModel", sb.toString());
        if (!address.isAccount() || address.isFlagIndentity()) {
            j = transaction.getAddressVoutSumSubVinSum(address);
            Log.d("BaseViewModel", "getBalance: getValueForAddress");
        } else {
            j = WalletUtil.m7076g(transaction);
            Log.d("BaseViewModel", "getBalance: getTransactionValueForHd");
        }
        return Util.m7025a(CAmount.toDecimalSatoshiDouble(Long.valueOf(j)).toString(), this.block_chain_type.name());
    }



    public String mo41363b(Transaction transaction, Address address) {
        String tempAddress;
        List txInList = transaction.getSelfTxInList();
        if (txInList == null || txInList.isEmpty()) {
            tempAddress = null;
        } else {
            tempAddress = ((TxIn) txInList.get(0)).getAddress();
            if (TextUtils.isEmpty(tempAddress)) {
                CTxDestination cTxDestination = ((TxIn) txInList.get(0)).getCTxDestination();
                if (cTxDestination != null && !(cTxDestination instanceof CNoDestination)) {
                    tempAddress = AddressUtils.getAddressString(cTxDestination, this.vCashCore.getWalletByChainType(this.block_chain_type).getChainParams());
                }
            }
        }
        if (!TextUtils.isEmpty(tempAddress)) {
            if (!TextUtils.equals(tempAddress, address.getAddressString(this.block_chain_type))) {
                return tempAddress;
            }
        }
        List d = transaction.getSelfTxOutList();
        if (d != null && !d.isEmpty()) {
            tempAddress = ((TxOut) d.get(0)).getAddress();
            if (TextUtils.isEmpty(tempAddress)) {
                CTxDestination cTxDestination = ((TxOut) d.get(0)).getScriptCTxDestination();
                if (cTxDestination != null && !(cTxDestination instanceof CNoDestination)) {
                    tempAddress = AddressUtils.getAddressString(cTxDestination, this.vCashCore.getWalletByChainType(this.block_chain_type).getChainParams());
                }
            }
        }
        if (!TextUtils.isEmpty(tempAddress)) {
            return tempAddress;
        }
        return address.getAddressString(this.block_chain_type);
    }


    //915 mo41510O
    public void checkWalletTypeAndEnterMain() {
        if (this.vCashCore.getWalletType() == WalletType.HOT) {
            boolean noticeCache = false;
            if (SharedPreferencesUtil.getSharedPreferencesUtil().getBooleanValue("is_sync_block_with_anon_tx", false, getContext())) {
                ((VBlockChainModel) this.vCashCore.getWalletByChainType(new BLOCK_CHAIN_TYPE[0]).getSelfBlockChainModel()).setSyncAnymousFlag(3);
            } else {
                ((VBlockChainModel) this.vCashCore.getWalletByChainType(new BLOCK_CHAIN_TYPE[0]).getSelfBlockChainModel()).setSyncAnymousFlag(1);
            }
            Wallet wallet = this.vCashCore.getWalletByChainType(BLOCK_CHAIN_TYPE.VCASH);
           //Wallet i2 = this.vCashCore.getWalletByChainType(BLOCK_CHAIN_TYPE.BITCOIN);
            if (wallet.getSelfAddressModel().getAllUsingTxDesMapAddress().size() + wallet.getSelfAddressModel().getSelfLinkedMapValueList().size() > 500) {
                noticeCache = true;
            }
//            if (i.mo44371F().mo44909e() + i2.mo44371F().mo44909e() > 60000) {
//                z = true;
//            }
            if (!noticeCache || wallet.getSelfAddressModel().isAddressSyncFinish()) {
                startActivity(new Intent(this.context, WalletMainActivity.class));
                ActivityManager.getInstance().removeAndFinishAll(WalletMainActivity.class);
                return;
            }
            //注意地址太多了
            //startActivity(new Intent(this.context, NoticeCacheActivity.class));
            finish();
            return;
        }
        //冷钱包删了
    }

}
