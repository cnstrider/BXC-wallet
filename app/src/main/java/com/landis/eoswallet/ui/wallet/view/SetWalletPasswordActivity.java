package com.landis.eoswallet.ui.wallet.view;

import android.text.InputFilter;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.databinding.ActivitySetWalletPasswordBinding;
import com.landis.eoswallet.event.Event;
import com.landis.eoswallet.filter.EditInputFilter;
import com.landis.eoswallet.ui.wallet.viewmodel.WalletViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 设置钱包密码
 */
@Route(path = RouteConst.AV_SET_WALLET_PASSWORD)
public class SetWalletPasswordActivity extends BaseActivity<ActivitySetWalletPasswordBinding,
        WalletViewModel> {

    private WalletViewModel walletViewModel;

    @Autowired(name = "account_name")
    public String accountName;

    @Autowired(name = "pri_key")
    public String priKey;

    @Autowired(name = "pub_key")
    public String pubKey;

    @Override
    protected void init() {

        mDataBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        InputFilter[] inputFilters = {new EditInputFilter()};
        mDataBinding.editKey.setFilters(inputFilters);

        mDataBinding.btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletViewModel.savePassword(mDataBinding.editKey.getText().toString().trim(),
                        mDataBinding.editKeySure.getText().toString().trim());
            }
        });

    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(walletViewModel);
    }

    @Override
    protected void initViewModel() {
        ARouter.getInstance().inject(this);
        walletViewModel = new WalletViewModel();
        walletViewModel.setAccountName(accountName);
        walletViewModel.priKey = priKey;
        walletViewModel.pubKey = pubKey;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set_wallet_password;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void createOk(Event.CreateOk ok) {
        finish();
    }
}
