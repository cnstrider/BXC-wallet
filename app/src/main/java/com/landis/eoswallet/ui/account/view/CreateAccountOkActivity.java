package com.landis.eoswallet.ui.account.view;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.databinding.ActivityCreateAccountOkBinding;
import com.landis.eoswallet.event.Event;
import com.landis.eoswallet.ui.account.viewmodel.CreateAccountOkViewModel;

import org.greenrobot.eventbus.EventBus;

/**
 * 账户创建完成
 */
@Route(path = RouteConst.AV_CREATE_ACCOUNT_OK)
public class CreateAccountOkActivity extends BaseActivity<ActivityCreateAccountOkBinding,
        CreateAccountOkViewModel> {

    @Autowired(name = "account_name")
    public String accountName;

    @Autowired(name = "pri_key")
    public String priKey;

    @Autowired(name = "pub_key")
    public String pubKey;


    @Override
    protected void init() {
        mDataBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new Event.CreateOk());
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
        ARouter.getInstance().inject(this);
        mViewModel = new CreateAccountOkViewModel(pubKey, priKey, accountName);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_create_account_ok;
    }
}
