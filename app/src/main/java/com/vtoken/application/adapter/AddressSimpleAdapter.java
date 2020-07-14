package com.vtoken.application.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vtoken.application.R;
import com.vtoken.application.viewHolder.ViewHolder;
import com.vtoken.application.widget.dialog.FragmentDialog;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddressSimpleAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    /* renamed from: a */
    boolean f3300a = true;

    //f3301b
    RecyclerSimpleAdapter.RecyclerSimpleBindEvent recyclerSimpleBindEvent;

    /* renamed from: c */
    protected FragmentDialog f3302c;

    //f3303d
    private List<T> dataList;

    //f3304e
    private int commonAddressLayoutId;

    //f3305f
    private int commonModelBR;

    //f3306g
    private int emptyAddLayoutId;

    //f3307h
    private int emptyModelBR;

    //f3308i
    private Object emptyModel;
    /* access modifiers changed from: private */

    //f3309j
    public AddressItemClickEvent addressItemClickEvent;
    /* access modifiers changed from: private */

    /* renamed from: k */
    public C2555b f3310k;

    /* renamed from: l */
    private boolean f3311l = false;

    /* renamed from: m */
    private int f3312m;
    /* access modifiers changed from: private */

    /* renamed from: n */
    public boolean f3313n;

    //C2554a
    public interface AddressItemClickEvent {
        //mo38468a
        void onItemClick(int i);

        //mo38469a
        void onLongClick(ViewHolder viewHolder, int i);
    }

    /* renamed from: v.dimensional.adapter.AddressSimpleAdapter$b */
    public interface C2555b {
        /* renamed from: a */
        void mo38470a(int i, ViewHolder viewHolder);
    }

    public AddressSimpleAdapter(int i, int i2) {
        if (this.f3302c == null) {
            this.f3302c = new FragmentDialog();
        }
        this.commonAddressLayoutId = i;
        this.commonModelBR = i2;
    }

    /* renamed from: a */
    public void mo38459a(boolean z) {
        this.f3311l = z;
    }

    public AddressSimpleAdapter(int i, int i2, int i3) {
        this.commonAddressLayoutId = i;
        this.commonModelBR = i2;
        this.emptyAddLayoutId = i3;
        if (this.f3302c == null) {
            this.f3302c = new FragmentDialog();
        }
    }

    /* renamed from: a */
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        View root = viewHolder.getViewDataBinding().getRoot();
        if (getItemViewType(position) == 0) {
            if (!(this.emptyModelBR == 0 || this.emptyModel == null)) {
                viewHolder.getViewDataBinding().setVariable(this.emptyModelBR, this.emptyModel);
            }
            viewHolder.getViewDataBinding().executePendingBindings();
        } else {
            viewHolder.getViewDataBinding().setVariable(this.commonModelBR, this.dataList.get(position));
            viewHolder.getViewDataBinding().executePendingBindings();
            if (this.addressItemClickEvent != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (!AddressSimpleAdapter.this.f3313n) {
                            AddressSimpleAdapter.this.addressItemClickEvent.onItemClick(position);
                        }
                    }
                });
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        AddressSimpleAdapter.this.addressItemClickEvent.onLongClick(viewHolder, position);
                        return false;
                    }
                });
            }
            if (this.f3310k != null) {
                View findViewById = viewHolder.getViewDataBinding().getRoot().findViewById(R.id.right_image);
                if (findViewById != null) {
                    findViewById.setOnLongClickListener(new View.OnLongClickListener() {
                        public boolean onLongClick(View view) {
                            if (!AddressSimpleAdapter.this.f3313n) {
                                AddressSimpleAdapter.this.f3310k.mo38470a(position, viewHolder);
                            }
                            return false;
                        }
                    });
                }
            }
            if (position == this.dataList.size() - 1) {
                View dividerView = viewHolder.getViewDataBinding().getRoot().findViewById(R.id.divider_view);
                if (dividerView != null) {
                    dividerView.setVisibility(View.INVISIBLE);
                }
            }
        }
        RecyclerSimpleAdapter.RecyclerSimpleBindEvent bindEvent = this.recyclerSimpleBindEvent;
        if (bindEvent != null) {
            bindEvent.onBind(viewHolder, position);
        }
    }

    public void setOnItemRightIconClickListener(C2555b bVar) {
        this.f3310k = bVar;
    }

    public void setOnItemClickListener(AddressItemClickEvent aVar) {
        this.addressItemClickEvent = aVar;
    }

    /* renamed from: a */
    @NotNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding viewDataBinding;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0) {
            viewDataBinding = DataBindingUtil.inflate(inflater, this.emptyAddLayoutId, parent, false);
        } else {
            viewDataBinding = DataBindingUtil.inflate(inflater, this.commonAddressLayoutId, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(viewDataBinding.getRoot());
        viewHolder.setViewDataBinding(viewDataBinding);
        return viewHolder;
    }

//    public int getItemCount() {
//        if (this.emptyAddLayoutId != 0) {
//            List<T> list = this.dataList;
//            int i = 1;
//            if (list != null) {
//                i = 1 + list.size();
//            }
//            return i;
//        }
//        List<T> list2 = this.dataList;
//        return list2 == null ? 0 : list2.size();
//    }

    public int getItemCount() {
        List<T> list = this.dataList;
        if (this.emptyAddLayoutId != 0 &&list!=null&&list.size()==0) {
            return 1;
        }
        return list == null ? 0 : list.size();
    }

    //0 空数据或非法
    public int getItemViewType(int position) {
        List<T> list = this.dataList;
        if (list == null || list.size() == 0 || position > this.dataList.size() - 1) {
            return 0;
        }
        return 1;
    }

    //mo38456a
    public void setDataList(List<T> list) {
        this.dataList = new ArrayList();
        this.dataList.addAll(list);
    }

    /* renamed from: a */
    public boolean mo38460a() {
        return this.f3313n;
    }

    /* renamed from: b */
    public void mo38461b() {
        notifyItemChanged(this.f3312m);
        this.f3313n = false;
    }

    //mo38454a
    public void swapItem(int position1, int position2) {
        if (getItemViewType(position1) != 0 && getItemViewType(position2) != 0 && position1 <= this.dataList.size() && position2 <= this.dataList.size()) {
            Collections.swap(this.dataList, position1, position2);
            notifyItemMoved(position1, position2);
        }
    }

    //mo38462c
    public List<T> getDataList() {
        return this.dataList;
    }

    //mo38458a
    public void setBindEvent(RecyclerSimpleAdapter.RecyclerSimpleBindEvent simpleBindEvent) {
        this.recyclerSimpleBindEvent = simpleBindEvent;
    }

    //mo38455a
    public void setEmptyModel(Object obj) {
        this.emptyModel = obj;
    }

    //mo38453a
    public void setEmptyBR(int i) {
        this.emptyModelBR = i;
    }
}
