package com.vtoken.application.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_SETTLING

abstract class OnLoadMoreListener: RecyclerView.OnScrollListener() {

    private var countItem:Int=0;

    private var lastItem:Int=0
    private var isScrolled=false
    private lateinit var layoutManager: RecyclerView.LayoutManager

    protected abstract fun onLoading(countItem:Int,lastItem:Int)

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if(newState==SCROLL_STATE_DRAGGING||newState==SCROLL_STATE_SETTLING){
            isScrolled=true
        }else{
            isScrolled=false
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if(recyclerView.layoutManager is LinearLayoutManager){
            layoutManager=recyclerView.layoutManager!!
            countItem=layoutManager.itemCount
            lastItem=(layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
        }
        if(isScrolled&&countItem!=lastItem&&lastItem==countItem-1){
            onLoading(countItem,lastItem)
        }
    }
}