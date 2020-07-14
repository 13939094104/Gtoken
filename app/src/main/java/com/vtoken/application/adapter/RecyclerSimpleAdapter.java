package com.vtoken.application.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vtoken.application.R;
import com.vtoken.application.viewHolder.ViewHolder;
import com.vtoken.application.viewModel.BaseViewModel;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import java.util.ArrayList;
import java.util.List;

public class RecyclerSimpleAdapter<T> extends Adapter<ViewHolder> {

    //f3375a
    protected List<T> dataList;

    //f3376b
    protected int commonItemLayoutId;

    /* renamed from: c */
    protected int emptyLayoutId;

    //f3378d
    protected int commonItemBR;

    /* renamed from: e */
    protected int f3379e = -1;

    /* renamed from: f */
    protected Object f3380f;

    //f3381g
    public RecyclerSimpleAdapterClickEvent clickEvent;

    //915 f3414h
    protected RecyclerSimpleAdapterSelectEvent selectEvent;

    /* renamed from: i */
    protected RecyclerSimpleBindEvent f3383i;

    /* renamed from: j */
    protected boolean f3384j;

    protected BaseViewModel parentModel;

    protected int parentModelBR;



    //C2569a
    //915 C2573a
    public interface RecyclerSimpleBindEvent {
        void onBind(ViewHolder viewHolder, int i);
    }

    //C2570b
    public interface RecyclerSimpleAdapterClickEvent {
        //mo38520a
        void onClick(int position);

        //mo38521b
        void onLongClick(int position);
    }


    //915 C2575c
    public interface RecyclerSimpleAdapterSelectEvent {
        //915 mo38590a
        void onClick(int i, View view);

        //915 mo38591b
        void onLongClick(int i, View view);
    }

    public void setParentModel(BaseViewModel model,int id){
        this.parentModel=model;
        this.parentModelBR=id;
    }

    //mo38514a
    //915 mo38581a
    public void setRecyclerBindEvent(RecyclerSimpleBindEvent recyclerSimpleBindEvent) {
        this.f3383i = recyclerSimpleBindEvent;
    }

    public RecyclerSimpleAdapter(int comminItemLayoutId, int commonBR) {
        this.commonItemLayoutId = comminItemLayoutId;
        this.commonItemBR = commonBR;
    }

    public RecyclerSimpleAdapter(int i, int i2, int i3) {
        this.commonItemLayoutId = i;
        this.commonItemBR = i2;
        this.emptyLayoutId = i3;
    }

    public RecyclerSimpleAdapter(int i, int i2, int i3, int i4, Object obj) {
        this.commonItemLayoutId = i;
        this.commonItemBR = i2;
        this.emptyLayoutId = i3;
        this.f3379e = i4;
        this.f3380f = obj;
    }

    //915 mo38582a
    public void setSelcetEvent(RecyclerSimpleAdapterSelectEvent selectEvent) {
        this.selectEvent = selectEvent;
    }


    public void setOnItemClickListener(RecyclerSimpleAdapterClickEvent event) {
        this.clickEvent = event;
    }

    /* renamed from: a */
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        ViewDataBinding viewDataBinding;
        LayoutInflater from = LayoutInflater.from(viewGroup.getContext());
        int i2 = this.emptyLayoutId;
        if (i2 == 0 || viewType != 0) {
            viewDataBinding = DataBindingUtil.inflate(from, this.commonItemLayoutId, viewGroup, false);
        } else {
            viewDataBinding = DataBindingUtil.inflate(from, i2, viewGroup, false);
        }
        ViewHolder viewHolder = new ViewHolder(viewDataBinding.getRoot());
        viewHolder.setViewDataBinding(viewDataBinding);
        return viewHolder;
    }

    /* renamed from: a */
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        View root = viewHolder.getViewDataBinding().getRoot();
        if (getItemViewType(i) != 0) {
            viewHolder.getViewDataBinding().setVariable(this.commonItemBR, this.dataList.get(i));
            if(parentModel!=null){
                viewHolder.getViewDataBinding().setVariable(this.parentModelBR,parentModel);
            }
            viewHolder.getViewDataBinding().executePendingBindings();
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (RecyclerSimpleAdapter.this.clickEvent != null) {
                            RecyclerSimpleAdapter.this.clickEvent.onClick(i);
                        }
                        if (RecyclerSimpleAdapter.this.selectEvent != null) {
                            RecyclerSimpleAdapter.this.selectEvent.onClick(i, view);
                        }
                    }
                });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        if (RecyclerSimpleAdapter.this.clickEvent != null) {
                            RecyclerSimpleAdapter.this.clickEvent.onLongClick(i);
                        }
                        if (RecyclerSimpleAdapter.this.selectEvent != null) {
                            RecyclerSimpleAdapter.this.selectEvent.onLongClick(i, view);
                        }
                        return false;
                    }
                });
            if (i == this.dataList.size() - 1) {
                View findViewById = viewHolder.getViewDataBinding().getRoot().findViewById(R.id.divider_view);
                if (findViewById != null) {
                    findViewById.setVisibility(View.INVISIBLE);
                }
            }
        } else if (!(this.f3380f == null || this.f3379e == -1)) {
            viewHolder.getViewDataBinding().setVariable(this.f3379e, this.f3380f);
        }
        RecyclerSimpleBindEvent aVar = this.f3383i;
        if (aVar != null) {
            aVar.onBind(viewHolder, i);
        }
    }

    public int getItemCount() {
        if (this.emptyLayoutId == 0) {
            List<T> list = this.dataList;
            return list == null ? 0 : list.size();
        }
        List<T> list2 = this.dataList;
        return (list2 == null || list2.isEmpty()) ? 1 : this.dataList.size();
    }

    //mo38512a
    //915 mo38579a
    public void setDataList(List<T> list) {
        List<T> list2 = this.dataList;
        if (list2 == null) {
            this.dataList = new ArrayList();
        } else {
            list2.clear();
        }
        if (list == null || list.isEmpty()) {
            Log.e("RecyclerSimpleAdapter", "setData: but data is empty");
        } else {
            this.dataList.addAll(list);
        }
    }

    public int getItemViewType(int i) {
        List<T> list = this.dataList;
        return (list == null || list.isEmpty()) ? 0 : 1;
    }

    //mo38510a
    //915 mo38577a
    public List<T> getDataList() {
        return this.dataList;
    }

    /* renamed from: a */
    public void mo38515a(boolean z) {
        this.f3384j = z;
    }
}
