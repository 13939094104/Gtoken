package com.vtoken.application.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vtoken.application.model.Node;
import com.vtoken.application.viewHolder.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class NodeSimpleAdapter extends RecyclerView.Adapter<ViewHolder> {

    /* renamed from: a */
    RecyclerSimpleAdapter.RecyclerSimpleBindEvent f3354a;

    //f3355b
    NodeSimpleAdapterEvent nodeSimpleAdapterEvent;
    /* access modifiers changed from: private */

    //f3356c
    public List<Node> nodeList;

    //f3357d
    private int groupNodeLayoutId;

    //f3358e
    private int vIdentityLayoutId;

    //f3359f
    private int emptyLayoutId;

    //f3360g
    private int nodeGroupBR;

    /* renamed from: h */
    private int f3361h;

    //f3362i
    private int emptyItemBR = -1;

    //f3363j
    private Object emptyViewModel;

    //C2566a
    public interface NodeSimpleAdapterEvent {
        //mo38506a
        void onEmptyItemClick(View view);

        //mo38507a
        void onNodeExpandStausChange(Node node, boolean z);

        //mo38508a
        boolean onLongClick(View view, ViewHolder viewHolder, Node node);

        //mo38509a
        boolean onCommonHolderClick(View view, Node node);
    }

    public NodeSimpleAdapter(int i, int i2, int i3, int i4, int i5, int i6, Object obj) {
        this.groupNodeLayoutId = i;
        this.nodeGroupBR = i2;
        this.vIdentityLayoutId = i3;
        this.f3361h = i4;
        this.emptyLayoutId = i5;
        this.emptyItemBR = i6;
        this.emptyViewModel = obj;
    }

    /* renamed from: a */
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewDataBinding viewDataBinding;
        LayoutInflater from = LayoutInflater.from(viewGroup.getContext());
        switch (i) {
            case 1:
                viewDataBinding = DataBindingUtil.inflate(from, this.groupNodeLayoutId, viewGroup, false);
                break;
            case 2:
                viewDataBinding = DataBindingUtil.inflate(from, this.vIdentityLayoutId, viewGroup, false);
                break;
            case 3:
                viewDataBinding = DataBindingUtil.inflate(from, this.emptyLayoutId, viewGroup, false);
                break;
            default:
                viewDataBinding = null;
                break;
        }
        ViewHolder viewHolder = new ViewHolder(viewDataBinding.getRoot());
        viewHolder.setViewDataBinding(viewDataBinding);
        return viewHolder;
    }

    /* renamed from: a */
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        switch (getItemViewType(i)) {
            case 1:
                List<Node> list = this.nodeList;
                if (list != null && i < list.size()) {
                    viewHolder.getViewDataBinding().setVariable(this.nodeGroupBR, this.nodeList.get(i));
                    viewHolder.itemView.setTag(this.nodeList.get(i));
                }
                if (viewHolder.itemView != null) {
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            if (NodeSimpleAdapter.this.nodeSimpleAdapterEvent != null) {
                                Node node = (Node) viewHolder.itemView.getTag();
                                if (!NodeSimpleAdapter.this.nodeSimpleAdapterEvent.onCommonHolderClick(view, node)) {
                                    NodeSimpleAdapter.this.switchNodeExpandStatus(node);
                                }
                            }
                        }
                    });
                    viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        public boolean onLongClick(View view) {
                            return NodeSimpleAdapter.this.nodeSimpleAdapterEvent != null && NodeSimpleAdapter.this.nodeSimpleAdapterEvent.onLongClick(view, viewHolder, (Node) NodeSimpleAdapter.this.nodeList.get(i));
                        }
                    });
                    break;
                }
                break;
            case 2:
                List<Node> list2 = this.nodeList;
                if (list2 != null && i < list2.size()) {
                    viewHolder.getViewDataBinding().setVariable(this.f3361h, this.nodeList.get(i));
                    viewHolder.itemView.setTag(this.nodeList.get(i));
                }
                if (viewHolder.itemView != null) {
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            NodeSimpleAdapter.this.nodeSimpleAdapterEvent.onCommonHolderClick(view, (Node) NodeSimpleAdapter.this.nodeList.get(i));
                        }
                    });
                }
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        return NodeSimpleAdapter.this.nodeSimpleAdapterEvent != null && NodeSimpleAdapter.this.nodeSimpleAdapterEvent.onLongClick(view, viewHolder, (Node) NodeSimpleAdapter.this.nodeList.get(i));
                    }
                });
                break;
            case 3:
                if (!(this.emptyItemBR == -1 || this.emptyViewModel == null)) {
                    viewHolder.getViewDataBinding().setVariable(this.emptyItemBR, this.emptyViewModel);
                }
                if (viewHolder.itemView != null) {
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            if (NodeSimpleAdapter.this.nodeSimpleAdapterEvent != null) {
                                NodeSimpleAdapter.this.nodeSimpleAdapterEvent.onEmptyItemClick(view);
                            }
                        }
                    });
                    break;
                }
                break;
        }
        viewHolder.getViewDataBinding().executePendingBindings();
        RecyclerSimpleAdapter.RecyclerSimpleBindEvent aVar = this.f3354a;
        if (aVar != null) {
            aVar.onBind(viewHolder, i);
        }
    }

    //m3719c
    public void switchNodeExpandStatus(Node node) {
        if (node != null) {
            if (node.isExpand.get()) {
                closeNode(node);
            } else {
                expandNode(node);
            }
        }
    }

    //mo38499a
    public void closeNode(Node node) {
        if (node != null && node.isExpand.get()) {
            ObservableList nodeList = node.getNodeList();
            int indexOf = this.nodeList.indexOf(node);
            if (nodeList != null) {
                this.nodeList.removeAll(nodeList);
                notifyItemRangeRemoved(indexOf + 1, nodeList.size());
            }
            NodeSimpleAdapterEvent event = this.nodeSimpleAdapterEvent;
            if (event != null) {
                event.onNodeExpandStausChange(node, false);
            }
            node.isExpand.set(false);
        }
    }

    //mo38500b
    public void expandNode(Node node) {
        if (node != null && !node.isExpand.get()) {
            ObservableList nodeList = node.getNodeList();
            int indexOf = this.nodeList.indexOf(node);
            if (nodeList != null && !nodeList.isEmpty()) {
                int i = indexOf + 1;
                this.nodeList.addAll(i, nodeList);
                notifyItemRangeInserted(i, nodeList.size());
            }
            NodeSimpleAdapterEvent event = this.nodeSimpleAdapterEvent;
            if (event != null) {
                event.onNodeExpandStausChange(node, true);
            }
            node.isExpand.set(true);
        }
    }

    public int getItemCount() {
        List<Node> list = this.nodeList;
        if (list == null || list.isEmpty()) {
            return 1;
        }
        return this.nodeList.size();
    }

    //mo38495a
    public void setDataList(List<Node> list) {
        this.nodeList = new ArrayList();
        if (list == null || list.isEmpty()) {
            Log.e("NodeSimpleAdapter", "setData: but data is empty");
        } else {
            this.nodeList.addAll(list);
        }
    }

    public int getItemViewType(int i) {
        List<Node> list = this.nodeList;
        if (list == null || list.isEmpty()) {
            return 3;
        }
        return ((Node) this.nodeList.get(i)).getNodeType();
    }

    /* renamed from: a */
    public List<Node> mo38493a() {
        return this.nodeList;
    }

    //mo38497a
    public void setNodeApaterEvent(NodeSimpleAdapterEvent event) {
        this.nodeSimpleAdapterEvent = event;
    }

    /* renamed from: a */
    public void mo38498a(RecyclerSimpleAdapter.RecyclerSimpleBindEvent event) {
        this.f3354a = event;
    }
}
