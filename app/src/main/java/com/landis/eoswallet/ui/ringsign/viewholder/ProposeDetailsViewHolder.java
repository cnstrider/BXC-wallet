package com.landis.eoswallet.ui.ringsign.viewholder;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.landis.eoswallet.R;
import com.landis.eoswallet.base.viewholder.BaseViewHolder;
import com.landis.eoswallet.databinding.ItemProposeDetailsBinding;
import com.landis.eoswallet.ui.ringsign.viewmodel.item.ProposeDetailesItemViewModel;

public class ProposeDetailsViewHolder extends BaseViewHolder<ItemProposeDetailsBinding, ProposeDetailesItemViewModel> {
    public ProposeDetailsViewHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.item_propose_details);
    }

    @Override
    protected void initViewModel() {
        mViewModel = new ProposeDetailesItemViewModel();
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void init() {
    }
}
