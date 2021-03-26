package com.landis.eoswallet.ui.account.view;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.databinding.ActivityAccountDetailBinding;
import com.landis.eoswallet.ui.account.viewmodel.AccountDetailViewModel;

@Route(path= RouteConst.AV_ACCOUNT_DETAIL)
public class AccountDetailActivity extends BaseActivity<ActivityAccountDetailBinding,AccountDetailViewModel> {


    @Override
    protected void init() {
        setAppBarView(mDataBinding.appbar);
        mDataBinding.ivBack.setOnClickListener(v -> finish());
        mViewModel.createQrImage(mDataBinding.imageQr);
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void initViewModel() {
        mViewModel = new AccountDetailViewModel();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_account_detail;
    }
}
