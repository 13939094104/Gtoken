package com.vtoken.application.util;


import android.annotation.SuppressLint;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.graphics.Rect;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vtoken.application.ApplicationLoader;
import com.vtoken.application.R;
import com.vtoken.application.adapter.PullLoadingAdapter;
import com.vtoken.application.viewModel.wallet.WalletAddressDetailViewModel;
import com.vtoken.application.widget.DatePicker;
import com.vtoken.application.widget.WrapContentLinearLayoutManager;


import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter;
import vdsMain.BLOCK_CHAIN_TYPE;
import vdsMain.model.Address;
import vdsMain.tool.DeviceUtil;
import vdsMain.transaction.Transaction;
import vdsMain.transaction.TransactionConfirmType;

//bbd
public class BindUtil {


    @BindingAdapter({"imageViewSrc"})
    public static void setImageSrcWithId(ImageView imageView, int resourcesId) {
        imageView.setImageResource(resourcesId);
    }

    @BindingAdapter({"backSrc"})
    public static void setBackWithId(View view, int resourcesId) {
        view.setBackgroundResource(resourcesId);
    }

    @BindingAdapter("android:layout_marginLeft")
    public static void setLeftMargin(View view, int leftMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(leftMargin, layoutParams.topMargin,
                layoutParams.rightMargin,layoutParams.bottomMargin);
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter({"pullRecyclerViewAdapter"})
    public static void setPullRecyclerViewAdapter(RecyclerView recyclerView, PullLoadingAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.VERTICAL, false));
        recyclerView.addOnScrollListener(adapter.getLoadingMoreListener());
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter({"recyclerViewAdapterHor"})
    public static void m6f895b(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(),LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter({"recyclerViewAdapter"})
    public static void m6895b(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        LinearLayoutManager manager=new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter(value = {"recyclerViewAlphaInAnimationAdapter"})
    public static void recyclerViewAlphaInAnimationAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        LinearLayoutManager manager=new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        AlphaInAnimationAdapter animAdapter=new AlphaInAnimationAdapter(adapter);
        animAdapter.setDuration(1000);
        //animAdapter.setInterpolator(new OvershootInterpolator());
        animAdapter.setFirstOnly(true);
        recyclerView.setAdapter(animAdapter);
    }

    @BindingAdapter(value = {"recyclerViewSlideInLeftAnimationAdapter"})
    public static void recyclerViewAdapterWithAnimationAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        LinearLayoutManager manager=new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        SlideInLeftAnimationAdapter animAdapter=new SlideInLeftAnimationAdapter(adapter);
        animAdapter.setDuration(400);
        //animAdapter.setInterpolator(new OvershootInterpolator());
        animAdapter.setFirstOnly(false);
        recyclerView.setAdapter(animAdapter);
    }

    @BindingAdapter({"recyclerViewAdapterReverseStack"})
    public static void recyclerViewAdapterReverseStack(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        LinearLayoutManager linearLayoutManager=new WrapContentLinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter({"recyclerViewAdapterStaggeredGrid"})
    public static void recyclerViewAdapterStaggeredGrid(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(5,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.offsetChildrenHorizontal(22);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }



    @BindingAdapter({"year", "month", "day"})
    /* renamed from: a */
    public static void m6892a(DatePicker datePicker, int year, int month, int day) {
        datePicker.setDatePickerValue(day, month, year);
    }

    @BindingAdapter({"setTxLayout"})
    /* renamed from: a */
    public static void m6880a(LinearLayout linearLayout, Address address) {
        Transaction transaction;
        SpannableStringBuilder spannableStringBuilder;
        if (address == null) {
            linearLayout.setVisibility(View.GONE);
            return;
        }
        linearLayout.setVisibility(View.VISIBLE);
        ImageView imageView = (ImageView) linearLayout.findViewById(R.id.tx_icon);
        TextView textView = (TextView) linearLayout.findViewById(R.id.tx_value);
        BLOCK_CHAIN_TYPE blockChainType = ApplicationLoader.getBlockChainType();
        long transactionBalance = 0;
        if (!address.isAccount() || address.isFlagIndentity()) {
            transaction = address.getNewestTransaction(blockChainType);
            if (transaction != null) {
                transactionBalance = transaction.getAddressVoutSumSubVinSum(address);
            }
        } else {
            Transaction a = ApplicationLoader.getVcashCore().mo43741a(address.getAccount(), TransactionConfirmType.ALL, blockChainType);
            if (a != null) {
                //transactionBalance = WalletUtil.m7076g(a);
            }
            transaction = a;
        }
        //TODO
//        if (transaction == null) {
//            imageView.setVisibility(8);
//            textView.setVisibility(8);
//            return;
//        }
//        imageView.setVisibility(0);
//        textView.setVisibility(0);
//        int a2 = bbw.m7014a(blockChainType);
//        String b = CAmount.m10852b(Long.valueOf(transactionBalance));
//        if (new BigDecimal(b).compareTo(BigDecimal.ZERO) == 1) {
//            StringBuilder sb = new StringBuilder();
//            sb.append("+");
//            sb.append(bbw.m7023a(b, a2));
//            spannableStringBuilder = new SpannableStringBuilder(sb.toString());
//            spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ApplicationLoader.m3620a(), R.color.wallet_yellow)), 0, spannableStringBuilder.length(), 34);
//        } else {
//            spannableStringBuilder = new SpannableStringBuilder(bbw.m7023a(b, a2));
//            spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ApplicationLoader.m3620a(), R.color.white)), 0, spannableStringBuilder.length(), 34);
//        }
//        textView.setText(spannableStringBuilder);
//        switch (blockChainType) {
//            case VCASH:
//                if (transaction.mo43271w()) {
//                    imageView.setBackgroundResource(R.drawable.tx_confirm);
//                    break;
//                } else {
//                    imageView.setBackgroundResource(R.drawable.tx_unconfirm_v);
//                    break;
//                }
//            case BITCOIN:
//                if (transaction.mo43271w()) {
//                    imageView.setBackgroundResource(R.drawable.tx_confirm);
//                    break;
//                } else {
//                    imageView.setBackgroundResource(R.drawable.tx_unconfirm_btc);
//                    break;
//                }
//        }
    }

    @BindingAdapter({"setLongTimeStampFormat"})
    //m6870a
    public static void setLongTimeStampFormat(TextView textView, Long timestamp) {
        textView.setText(DateUtil.INSTANCE.formatTimeStampDefault(timestamp*1000,"yyyy.MM.dd HH:mm:ss"));
    }

    @BindingAdapter({"setTxTimeStampFormat"})
    //m6870a
    public static void setTxTimeStampFormat(TextView textView, Transaction transaction) {
        textView.setText(DateUtil.INSTANCE.formatTimeStampDefault(transaction.getTimeStamp()*1000,"yyyy.MM.dd HH:mm:ss"));
    }

    @BindingAdapter({"setIntTextView"})
    //m6870a
    public static void setIntTextView(TextView textView, Integer value) {
        textView.setText(String.valueOf(value));
    }

    @SuppressLint("ResourceAsColor")
    @BindingAdapter(value = {"walletAddressDetailModel", "transaction"}, requireAll = true)
    //@BindingAdapter({"setTxBalanceStyle"})
    //m6870a
    public static void setTxBalanceStyle(LinearLayout linearLayout, WalletAddressDetailViewModel viewModel,Transaction transaction) {
        TextView balanceTextView=linearLayout.findViewById(R.id.balance);
        ImageView symbolView=linearLayout.findViewById(R.id.symbol);
        String balance=viewModel.getTransactionBalanceBySelfAddress(transaction);
//        Long value=Long.valueOf(balance);
//        balanceTextView.setText(balance);
//        if(value>0){
//            balanceTextView.setTextColor(R.color.greentext);
//            symbolView.setImageResource(R.drawable.icon_vollar_green);
//        }else {
//            balanceTextView.setTextColor(R.color.mainyellowtext);
//            symbolView.setImageResource(R.drawable.icon_vollar_yellow);
//        }
    }



    @BindingAdapter({"setTxStatusText"})
    //m6870a
    public static void setTxStatusText(TextView textView, Transaction transaction) {
        if (transaction.isNotConfirmed() || transaction.isDefaultHash()) {
            textView.setText("失败");
        } else if (transaction.isConfirmed()) {
            textView.setText("已确认");
        } else {
            textView.setText("待确认");
        }
    }

    @BindingAdapter({"setTxStatusImage"})
    //m6870a
    public static void setTxStatusImage(ImageView imageView, Transaction transaction) {
        if (transaction.isNotConfirmed() || transaction.isDefaultHash()) {
            imageView.setImageResource(R.drawable.icon_defeated);
        } else if (transaction.isConfirmed()) {
            imageView.setImageResource(R.drawable.icon_success1);
        } else {
            imageView.setImageResource(R.drawable.icon_wait_comfirm);
        }
    }

    @BindingAdapter({"setTxMark"})
    //m6879a
    public static void setTxMark(TextView textView, Transaction transaction) {
        String remark = ApplicationLoader.getVcashCore().getRemarkByTxid(transaction.getTxId(), ApplicationLoader.getBlockChainType());
        if (TextUtils.isEmpty(remark)) {
            textView.setVisibility(View.GONE);
            return;
        }
        textView.setVisibility(View.VISIBLE);
        if (remark.length() > 5) {
            StringBuilder sb = new StringBuilder();
            sb.append(remark.substring(0, 5));
            sb.append("...");
            textView.setText(sb.toString());
            return;
        }
        textView.setText(remark);
    }

    @BindingAdapter({"setImageWithUrl"})
    //m6879a
    public static void setImageWithUrl(ImageView imageView, String url) {
        Glide.with(ApplicationLoader.applicationContext).load(url).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
    }
}
