package com.landis.eoswallet.ui.ringsign.view;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.databinding.ActivityCreateRingSignAccountBinding;
import com.landis.eoswallet.ui.ringsign.viewmodel.CreateRingSignAccountViewModel;
@Route(path = RouteConst.AV_CREATE_RING_SIGN_ACCOUNT)
public class CreateRingSignAccountActivity extends BaseActivity<ActivityCreateRingSignAccountBinding, CreateRingSignAccountViewModel> {
    @Override
    protected void init() {
        setAppBarView(mDataBinding.appbar);
        mDataBinding.ivRingSignBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void initViewModel() {
        mViewModel = new CreateRingSignAccountViewModel();
        mViewModel.init(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_create_ring_sign_account;
    }
}
