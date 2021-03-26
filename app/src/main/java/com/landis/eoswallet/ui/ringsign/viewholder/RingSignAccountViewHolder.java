package com.landis.eoswallet.ui.ringsign.viewholder;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.landis.eoswallet.R;
import com.landis.eoswallet.base.viewholder.BaseViewHolder;
import com.landis.eoswallet.databinding.ItemRingSignAccountBinding;
import com.landis.eoswallet.ui.ringsign.viewmodel.item.RingSignAccountItemViewModel;

public class RingSignAccountViewHolder extends BaseViewHolder<ItemRingSignAccountBinding, RingSignAccountItemViewModel> {
    public RingSignAccountViewHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.item_ring_sign_account);
    }

    @Override
    protected void initViewModel() {
        mViewModel = new RingSignAccountItemViewModel();
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void init() {

    }
}
