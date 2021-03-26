package com.landis.eoswallet.ui.home.adapter;

import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.landis.eoswallet.base.adapter.BaseAdapter;
import com.landis.eoswallet.net.model.AppInfo;
import com.landis.eoswallet.ui.home.viewholder.AppInfoViewHolder;

public class AppInfoAdapter extends BaseAdapter<RecyclerView.ViewHolder> {
    public AppInfoAdapter(@NonNull ObservableList<Object> dataList) {
        super(dataList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AppInfoViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Object data = mDataList.get(i);
        ((AppInfoViewHolder) viewHolder).getViewModel().setBaseItemModel((AppInfo) data);
    }
}
