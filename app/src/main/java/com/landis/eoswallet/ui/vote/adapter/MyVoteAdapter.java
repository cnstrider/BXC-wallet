package com.landis.eoswallet.ui.vote.adapter;

import android.content.Context;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.landis.eoswallet.base.adapter.BaseAdapter;
import com.landis.eoswallet.net.model.VoteNodesList;
import com.landis.eoswallet.ui.vote.viewholder.MyVoteViewHolder;

public class MyVoteAdapter extends BaseAdapter<RecyclerView.ViewHolder> {

    private Context context;

    public MyVoteAdapter(@NonNull ObservableList<Object> dataList,Context context) {
        super(dataList);
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        MyVoteViewHolder myVoteViewHolder = new MyVoteViewHolder(viewGroup);
        myVoteViewHolder.setContext(context);
        return myVoteViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Object data = mDataList.get(i);
        ((MyVoteViewHolder) viewHolder).getViewModel().setBaseItemModel((VoteNodesList.VoteNodes) data);
    }
}
