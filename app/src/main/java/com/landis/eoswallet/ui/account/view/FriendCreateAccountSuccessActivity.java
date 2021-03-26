package com.landis.eoswallet.ui.account.view;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.databinding.ActivityFriendCreateAccountSuccessBinding;
import com.landis.eoswallet.event.Event;
import com.landis.eoswallet.ui.account.viewmodel.FriendCreateAccountSuccessViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 朋友创建成功  设置密码界面
 */
@Route(path = RouteConst.AV_FRIEND_CREATE_ACCOUNT_SUCCESS)
public class FriendCreateAccountSuccessActivity extends BaseActivity<ActivityFriendCreateAccountSuccessBinding, FriendCreateAccountSuccessViewModel> {

    @Autowired(name = "account_name")
    public String accountName;

    @Autowired(name = "pri_key")
    public String priKey;

    @Autowired(name = "pub_key")
    public String pubKey;


    @Override
    protected void init() {

        mDataBinding.tvPrivateKeyCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.copyPriKey(getApplicationContext());
            }
        });

        mDataBinding.tvPublicKeyCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.copyPubKey(getApplicationContext());
            }
        });

        mDataBinding.btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.savePassword(mDataBinding.editPassword.getText().toString().trim()
                        , mDataBinding.editPasswordComplete.getText().toString().trim());
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
        mViewModel = new FriendCreateAccountSuccessViewModel(pubKey,
                priKey, accountName);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_friend_create_account_success;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void createOk(Event.CreateOk ok) {
        finish();
    }
}
