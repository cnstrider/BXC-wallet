package com.landis.eoswallet.ui.account.view;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.databinding.ActivityCreateAccountForFriendsBinding;
import com.landis.eoswallet.ui.account.viewmodel.CreateAccountForFriendsViewModel;

@Route(path = RouteConst.AV_CREATE_FOR_FRIEND)
public class CreateAccountForFriendsActivity extends BaseActivity<ActivityCreateAccountForFriendsBinding, CreateAccountForFriendsViewModel> {

    @Autowired(name = "key")
    public String publicKey;

    @Override
    protected void init() {
        setAppBarView(mDataBinding.appbar);
        mViewModel.createRandomAccount();
        mDataBinding.ivCustomCaptureBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDataBinding.btnCreateForFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.verifyPassword(CreateAccountForFriendsActivity.this);
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
        mViewModel = new CreateAccountForFriendsViewModel();
        mViewModel.setPubKey(publicKey);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_create_account_for_friends;
    }
}
