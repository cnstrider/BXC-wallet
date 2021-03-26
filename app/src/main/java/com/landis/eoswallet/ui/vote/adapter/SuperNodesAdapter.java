package com.landis.eoswallet.ui.vote.adapter;

import android.content.Context;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.landis.eoswallet.base.adapter.BaseAdapter;
import com.landis.eoswallet.net.model.VoteNodesList;
import com.landis.eoswallet.ui.vote.viewholder.SuperNodesViewHolder;

public class SuperNodesAdapter extends BaseAdapter<RecyclerView.ViewHolder> {

    private Context context;

    public SuperNodesAdapter(@NonNull ObservableList<Object> dataList,Context context) {
        super(dataList);
        this.context= context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        SuperNodesViewHolder superNodesViewHolder = new SuperNodesViewHolder(viewGroup);
        superNodesViewHolder.setContext(context);
        return superNodesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Object data = mDataList.get(i);
        ((SuperNodesViewHolder) viewHolder).getViewModel().setBaseItemModel((VoteNodesList.VoteNodes) data);
    }
}
