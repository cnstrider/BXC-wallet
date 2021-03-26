package com.landis.eoswallet.ui.ringsign.viewholder;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.landis.eoswallet.R;
import com.landis.eoswallet.base.viewholder.BaseViewHolder;
import com.landis.eoswallet.databinding.ItemConfigAuthBinding;
import com.landis.eoswallet.ui.ringsign.viewmodel.item.ConfigAuthItemViewModel;

public class ConfigAuthViewHolder extends BaseViewHolder<ItemConfigAuthBinding, ConfigAuthItemViewModel> {
    public ConfigAuthViewHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.item_config_auth);
    }

    @Override
    protected void initViewModel() {
        mViewModel = new ConfigAuthItemViewModel();
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void init() {
    }
}
