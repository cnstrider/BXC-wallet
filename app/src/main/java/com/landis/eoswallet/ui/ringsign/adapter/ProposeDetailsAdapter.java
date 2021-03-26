package com.landis.eoswallet.ui.ringsign.adapter;

import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.landis.eoswallet.base.adapter.BaseAdapter;
import com.landis.eoswallet.net.model.ProposeStateInfo;
import com.landis.eoswallet.ui.ringsign.viewholder.ProposeDetailsViewHolder;

public class ProposeDetailsAdapter extends BaseAdapter<RecyclerView.ViewHolder>{
    public ProposeDetailsAdapter(@NonNull ObservableList<Object> dataList) {
        super(dataList);


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ProposeDetailsViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Object data = mDataList.get(i);
        ((ProposeDetailsViewHolder) viewHolder).getViewModel().setBaseItemModel((ProposeStateInfo) data);
    }

}
