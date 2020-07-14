package com.vtoken.application.adapter

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import com.vtoken.application.viewHolder.ViewHolder

class FundPullLoadingAdapter<T> : PullLoadingAdapter<T> {

    var profitLayoutId=0
    var fundType=0

    constructor(commonLayoutId:Int, commonBR:Int,profitLayoutId:Int,loadingLayoutId:Int):super(commonLayoutId,commonBR,loadingLayoutId){
        this.profitLayoutId=profitLayoutId
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataList.size == 0 || position > this.dataList.size - 1) {
           return 0
        } else{
           return if(fundType==0) 1 else 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding: ViewDataBinding
        val inflater = LayoutInflater.from(parent.getContext())
        if (viewType == 0) {
            viewDataBinding = DataBindingUtil.inflate(inflater, this.loadingLayoutId, parent, false)
        }else if(viewType==1){
            viewDataBinding = DataBindingUtil.inflate(inflater, this.commonLayoutId, parent, false)
        } else {
            viewDataBinding = DataBindingUtil.inflate(inflater, this.profitLayoutId, parent, false)
        }
        val viewHolder = ViewHolder(viewDataBinding.root)
        viewHolder.viewDataBinding = viewDataBinding
        return viewHolder
    }

}

