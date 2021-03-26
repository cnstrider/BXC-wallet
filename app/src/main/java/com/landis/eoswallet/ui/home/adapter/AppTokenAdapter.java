package com.landis.eoswallet.ui.home.adapter;

import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.landis.eoswallet.base.adapter.BaseAdapter;
import com.landis.eoswallet.net.model.AppTokenInfo;
import com.landis.eoswallet.ui.home.viewholder.TokenViewHolder;

public class AppTokenAdapter extends BaseAdapter<RecyclerView.ViewHolder> {
    public AppTokenAdapter(@NonNull ObservableList<Object> dataList) {
        super(dataList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TokenViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Object data = mDataList.get(i);
        ((TokenViewHolder) viewHolder).getViewModel().setBaseItemModel((AppTokenInfo) data);
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        LogUtils.d("onViewRecycled");
        ((TokenViewHolder) holder).removeBalanceRefListener();
    }
}
