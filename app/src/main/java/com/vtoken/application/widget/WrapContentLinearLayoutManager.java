package com.vtoken.application.widget;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WrapContentLinearLayoutManager extends LinearLayoutManager {

    public WrapContentLinearLayoutManager(Context context){
        super(context);
    }

    public WrapContentLinearLayoutManager(Context context, @RecyclerView.Orientation int orientation, boolean reverseLayout) {
        super(context,orientation,reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
