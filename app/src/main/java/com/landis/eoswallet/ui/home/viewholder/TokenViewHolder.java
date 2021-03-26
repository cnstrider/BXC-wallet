package com.landis.eoswallet.ui.home.viewholder;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.viewholder.BaseViewHolder;
import com.landis.eoswallet.databinding.ItemAppTokenBinding;
import com.landis.eoswallet.net.listener.EosAccountRefListener;
import com.landis.eoswallet.net.manage.EosAccountManger;
import com.landis.eoswallet.ui.home.viewmodel.item.AppTokenInfoViewModel;

public class TokenViewHolder extends BaseViewHolder<ItemAppTokenBinding, AppTokenInfoViewModel> implements EosAccountRefListener {
    public TokenViewHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.item_app_token);
    }

    @Override
    protected void initViewModel() {
        mViewModel = new AppTokenInfoViewModel();
        EosAccountManger.getInstance().addEosAccountRefListeners(this);
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void init() {
    }

    @Override
    public void balanceFlushed() {
        mViewModel.notifyChange();
        LogUtils.d("刷新数据");
    }

    public void removeBalanceRefListener(){
        LogUtils.d("移除监听");
        EosAccountManger.getInstance().removeEosAccountRefListeners(this);
    }

}
