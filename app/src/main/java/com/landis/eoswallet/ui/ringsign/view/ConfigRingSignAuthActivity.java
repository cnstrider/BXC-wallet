package com.landis.eoswallet.ui.ringsign.view;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.databinding.ActivityConfigRingSignAuthBinding;
import com.landis.eoswallet.net.model.RingSignAccountInfo;
import com.landis.eoswallet.ui.ringsign.adapter.ConfigAuthAdapter;
import com.landis.eoswallet.ui.ringsign.viewmodel.ConfigRingSignAuthViewModel;

/**
 * 配置环签账号权限
 */
@Route(path = RouteConst.AV_CONFIG_RING_SIGN_AUTH)
public class ConfigRingSignAuthActivity extends BaseActivity<ActivityConfigRingSignAuthBinding, ConfigRingSignAuthViewModel> {

    @Autowired(name = "config_account")
    public RingSignAccountInfo configAccount;

    @Override
    protected void init() {
        setAppBarView(mDataBinding.appbar);

        mDataBinding.ivRingSignBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDataBinding.rvAuth.setAdapter(new ConfigAuthAdapter(mViewModel.authList));
        mDataBinding.rvAuth.setLayoutManager(new LinearLayoutManager(this));
        mViewModel.setConfigAccount(configAccount);

        mDataBinding.ivAddAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.checkAccount(mDataBinding.edConfigAuth.getText().toString().trim());
                mDataBinding.edConfigAuth.setText("");
            }
        });
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void initViewModel() {
        ARouter.getInstance().inject(this);
        mViewModel =new ConfigRingSignAuthViewModel();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_config_ring_sign_auth;
    }
}
