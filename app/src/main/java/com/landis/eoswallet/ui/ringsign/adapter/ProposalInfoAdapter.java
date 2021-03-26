package com.landis.eoswallet.ui.ringsign.adapter;

import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.landis.eoswallet.base.adapter.BaseAdapter;
import com.landis.eoswallet.net.model.ProposalInfo;
import com.landis.eoswallet.ui.ringsign.listener.DeteleListener;
import com.landis.eoswallet.ui.ringsign.viewholder.ProposalInfoViewHolder;

public class ProposalInfoAdapter extends BaseAdapter<RecyclerView.ViewHolder>  {
    public ProposalInfoAdapter(@NonNull ObservableList<Object> dataList) {
        super(dataList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ProposalInfoViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Object data = mDataList.get(i);
        ((ProposalInfoViewHolder) viewHolder).getViewModel().setBaseItemModel((ProposalInfo.RowsBean) data);
        ((ProposalInfoViewHolder) viewHolder).setDeteleListener(new DeteleListener() {
            @Override
            public void deleteItem(Object o) {
                mDataList.remove(o);
                notifyDataSetChanged();
            }
        });
    }

}
