package com.landis.eoswallet.ui.vote.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.landis.eoswallet.R;
import com.landis.eoswallet.base.viewholder.BaseViewHolder;
import com.landis.eoswallet.databinding.ItemSuperNodesBinding;
import com.landis.eoswallet.ui.vote.viewmodel.item.SuperNodesItemViewModel;

public class SuperNodesViewHolder extends BaseViewHolder<ItemSuperNodesBinding, SuperNodesItemViewModel> {

    public SuperNodesViewHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.item_super_nodes);
    }

    @Override
    protected void initViewModel() {
        mViewModel = new SuperNodesItemViewModel();

    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void init() {

    }

    public void setContext(Context context){
        mViewModel.setContext(context);
    }
}
