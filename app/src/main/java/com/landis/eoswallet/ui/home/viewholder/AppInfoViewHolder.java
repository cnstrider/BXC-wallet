package com.landis.eoswallet.ui.home.viewholder;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.landis.eoswallet.R;
import com.landis.eoswallet.base.viewholder.BaseViewHolder;
import com.landis.eoswallet.databinding.ItemAppinfoBinding;
import com.landis.eoswallet.ui.home.viewmodel.item.AppInfoViewModel;

public class AppInfoViewHolder extends BaseViewHolder<ItemAppinfoBinding, AppInfoViewModel> {

    public AppInfoViewHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.item_appinfo);
    }

    @Override
    protected void initViewModel() {
        mViewModel = new AppInfoViewModel();
    }

    @Override
    protected void bindViewModel() {
            mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void init() {

    }
}
