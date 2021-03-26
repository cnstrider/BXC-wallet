package com.landis.eoswallet.base.viewholder;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.landis.eoswallet.base.viewmodel.BaseItemViewModel;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;

public abstract class BaseViewHolder<DB extends ViewDataBinding, VM extends BaseItemViewModel>
        extends RecyclerView.ViewHolder {

    protected DB mDataBinding;

    protected VM mViewModel;
    public BaseViewHolder(@NonNull ViewGroup parent, @LayoutRes int layoutResId) {
        super(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutResId,
                parent, false).getRoot());
        mDataBinding = DataBindingUtil.getBinding(itemView);

        initViewModel();
        bindViewModel();

        init();
    }

    public VM getViewModel() {
        return mViewModel;
    }

    /**
     * 初始化ViewModel
     */
    protected abstract void initViewModel();

    /**
     * 绑定ViewModel
     */
    protected abstract void bindViewModel();

    /**
     * 初始化
     */
    protected abstract void init();

}
