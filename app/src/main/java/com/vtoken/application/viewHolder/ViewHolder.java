package com.vtoken.application.viewHolder;

import androidx.databinding.ViewDataBinding;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.vtoken.application.viewModel.BaseViewModel;

public class ViewHolder extends RecyclerView.ViewHolder {

    //f3299a
    private ViewDataBinding viewDataBinding;

    private BaseViewModel viewModel;

    public ViewHolder(View view) {
        super(view);
    }

    public ViewHolder(View view,BaseViewModel model) {
        super(view);
        viewModel=model;
    }

    //mo38450a
    public ViewDataBinding getViewDataBinding() {
        return this.viewDataBinding;
    }

    public BaseViewModel getViewModel(){return  this.viewModel;}

    //mo38451a
    public void setViewDataBinding(ViewDataBinding viewDataBinding) {
        this.viewDataBinding = viewDataBinding;
    }

    public void setViewModel(BaseViewModel model) {
        this.viewModel = model;
    }
}