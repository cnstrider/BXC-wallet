package com.landis.eoswallet.ui.account.view;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.databinding.ActivityAccountBinding;
import com.landis.eoswallet.event.Event;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 创建或导入账号
 */
@Route(path = RouteConst.AV_ACCOUNT)
public class AccountActivity extends BaseActivity<ActivityAccountBinding, BaseViewModel> {

    @Override
    protected void init() {
        if (!StringUtils.isEmpty(SPUtils.getInstance().getString(SpConst.WALLET_NAME))) {
            createOk(new Event.CreateOk());
        } else if (!StringUtils.isEmpty(SPUtils.getInstance().getString(SpConst.P_B_K)) && !StringUtils.isEmpty(SPUtils.getInstance().getString(SpConst.P_K))
                && StringUtils.isEmpty(SPUtils.getInstance().getString(SpConst.WALLET_NAME))) {
           ARouter.getInstance().build(RouteConst.AV_FRIEND_CREATE_ACCOUNT).navigation();
        }

        mDataBinding.tvFriendCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(RouteConst.AV_FRIEND_CREATE_ACCOUNT).navigation();
            }
        });

        mDataBinding.tvImportWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(RouteConst.AV_IMPORT_ACCOUNT).navigation();
            }
        });
    }

    @Override
    protected void bindViewModel() {
    }

    @Override
    protected void initViewModel() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_account;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void createOk(Event.CreateOk ok) {
        ARouter.getInstance().build(RouteConst.AV_MAIN).navigation(this);
        finish();
    }

}
