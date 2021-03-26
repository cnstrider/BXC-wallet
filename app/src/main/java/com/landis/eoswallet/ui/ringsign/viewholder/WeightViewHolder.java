package com.landis.eoswallet.ui.ringsign.viewholder;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.landis.eoswallet.R;
import com.landis.eoswallet.base.viewholder.BaseViewHolder;
import com.landis.eoswallet.databinding.ItemWeightsDetailsBinding;
import com.landis.eoswallet.ui.ringsign.viewmodel.item.WeightItemViewModel;

public class WeightViewHolder extends BaseViewHolder<ItemWeightsDetailsBinding, WeightItemViewModel> {
    public WeightViewHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.item_weights_details);
    }

    @Override
    protected void initViewModel() {
        mViewModel = new WeightItemViewModel();
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void init() {
    }
}
