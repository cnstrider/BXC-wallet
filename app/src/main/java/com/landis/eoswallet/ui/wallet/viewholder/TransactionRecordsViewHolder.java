package com.landis.eoswallet.ui.wallet.viewholder;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.landis.eoswallet.R;
import com.landis.eoswallet.base.viewholder.BaseViewHolder;
import com.landis.eoswallet.databinding.ItemTransactionRecordsBinding;
import com.landis.eoswallet.ui.wallet.viewmodel.item.TransactionRecordsViewModel;

public class TransactionRecordsViewHolder extends BaseViewHolder<ItemTransactionRecordsBinding, TransactionRecordsViewModel> {


    public TransactionRecordsViewHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.item_transaction_records);
    }

    @Override
    protected void initViewModel() {
        mViewModel = new TransactionRecordsViewModel();
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void init() {

    }
}
