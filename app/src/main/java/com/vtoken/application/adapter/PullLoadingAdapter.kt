package com.vtoken.application.adapter

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.vtoken.application.viewHolder.ViewHolder

open class PullLoadingAdapter<T>(var commonLayoutId:Int,var commonBR:Int,var loadingLayoutId:Int) : RecyclerView.Adapter<ViewHolder>() {

    var curPage=1

    var pageSize=10

    var hasMore=true

    var loadingMoreListener:OnLoadMoreListener

    protected var dataList: ArrayList<T> = ArrayList()

    var itemClickEvent: ItemClickEvent? = null

    var pullLoadingEvent: PullLoadingEvent? = null

    var recyclerSimpleBindEvent: RecyclerSimpleAdapter.RecyclerSimpleBindEvent?=null

    var isLoading:Boolean=false

    init {
        loadingMoreListener=object : OnLoadMoreListener(){
            override fun onLoading(countItem: Int, lastItem: Int) {
                if(pullLoadingEvent!=null&&hasMore){
                    pullLoadingEvent!!.onRequestLoadingData()
                    isLoading=true
                }
            }
        }
    }

    interface PullLoadingEvent{
        fun onRequestLoadingData()
    }

    interface ItemClickEvent {

        fun onClick(position: Int)

        fun onLongClick(viewHolder: ViewHolder, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding: ViewDataBinding
        val inflater = LayoutInflater.from(parent.getContext())
        if (viewType == 0) {
            viewDataBinding = DataBindingUtil.inflate(inflater, this.loadingLayoutId, parent, false)
        } else {
            viewDataBinding = DataBindingUtil.inflate(inflater, this.commonLayoutId, parent, false)
        }
        val viewHolder = ViewHolder(viewDataBinding.root)
        viewHolder.viewDataBinding = viewDataBinding
        return viewHolder
    }

    override fun getItemCount(): Int {
        if(hasMore||isLoading){
            return dataList.size+1
        }else{
            return dataList.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataList.size == 0 || position > this.dataList.size - 1) {
            0
        } else 1
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if(getItemViewType(position)!=0){
            viewHolder.viewDataBinding.setVariable(commonBR, dataList.get(position))
            viewHolder.viewDataBinding.executePendingBindings()
            if (this.itemClickEvent != null) {
                viewHolder.itemView.setOnClickListener {
                    this@PullLoadingAdapter.itemClickEvent!!.onClick(position)
                }
                viewHolder.itemView.setOnLongClickListener {
                    this@PullLoadingAdapter.itemClickEvent!!.onLongClick(viewHolder,position)
                    return@setOnLongClickListener false
                }
            }
        }
        val bindEvent = this.recyclerSimpleBindEvent
        if (bindEvent != null) {
            bindEvent.onBind(viewHolder, position)
        }
    }

    fun setDataList(list:List<T>){
        curPage++
        this.dataList.clear();
        this.dataList.addAll(list)
        if(list.size>=pageSize)
            hasMore=true
        else
            hasMore=false
        notifyDataSetChanged()
    }

    fun setFullDataList(list:List<T>,offset:Int){
        this.dataList.clear();
        this.dataList.addAll(list)
        isLoading=false
        if(offset>=pageSize)
            hasMore=true
        else
            hasMore=false
        notifyDataSetChanged()
    }

    fun addDataList(list:List<T>){
        curPage++
        isLoading=false
        val size=list.size
        if(size>0)
        {
            this.dataList.addAll(list)
            if(size>=pageSize)
                hasMore=true
            else
                hasMore=false
            notifyDataSetChanged()
        }else{
            hasMore=false;
        }
    }

}

