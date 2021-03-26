package com.landis.eoswallet.ui.wallet.adapter;

import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.landis.eoswallet.base.adapter.BaseAdapter;
import com.landis.eoswallet.net.model.TokenTransactionRecord;
import com.landis.eoswallet.ui.wallet.viewholder.TransactionRecordsViewHolder;

public class TransactionRecordsAdapter extends BaseAdapter<RecyclerView.ViewHolder> {


    public TransactionRecordsAdapter(@NonNull ObservableList<Object> dataList) {
        super(dataList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TransactionRecordsViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Object data = mDataList.get(i);
        ((TransactionRecordsViewHolder) viewHolder).getViewModel().setBaseItemModel((TokenTransactionRecord) data);
    }
}
