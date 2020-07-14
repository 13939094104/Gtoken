package com.vtoken.application.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.trello.rxlifecycle2.components.RxFragment;

//bby
public class BaseFragment extends RxFragment {

    //f9428a
    public Context context;

    //f9429b
    public Activity activity;

    //f9430c
    protected RecyclerView recyclerView;

    @RequiresApi(api = 23)
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.activity = getActivity();
        this.context = getContext();
    }

    //mo41260a
    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void mo41262b() {
        this.recyclerView.scheduleLayoutAnimation();
    }
}
