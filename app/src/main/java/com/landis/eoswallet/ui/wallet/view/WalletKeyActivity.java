package com.landis.eoswallet.ui.wallet.view;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.databinding.ActivityWalletKeyBinding;
import com.landis.eoswallet.util.CopyUtils;

@Route(path = RouteConst.AV_WALLET_KEY)
public class WalletKeyActivity extends BaseActivity<ActivityWalletKeyBinding, BaseViewModel> {

    @Autowired(name = RouteConst.WALLET_KEY)
    public String key;
    @Autowired(name = RouteConst.TITLE)
    public String title;
    @Autowired(name = RouteConst.OPERATION_TEXT)
    public String operationText;

    @Override
    protected void init() {
        mDataBinding.tvTitle.setText(title);
        mDataBinding.tvKey.setText(key);
        mDataBinding.tvCopy.setText(getString(R.string.copy)+operationText);
        mDataBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDataBinding.tvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CopyUtils.copy(v.getContext(),"eos_key",key);
                ToastUtils.showShort(getString(R.string.copy_success));
            }
        });
    }

    @Override
    protected void bindViewModel() {

    }

    @Override
    protected void initViewModel() {
        ARouter.getInstance().inject(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wallet_key;
    }
}
