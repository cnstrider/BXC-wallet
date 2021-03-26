package com.landis.eoswallet.ui.wallet.view;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.databinding.ActivityTransactionBinding;
import com.landis.eoswallet.ui.wallet.viewmodel.TransactionViewModel;

/**
 * 转账
 */
@Route(path = RouteConst.AV_TRANSACTION)
public class TransactionActivity extends BaseActivity<ActivityTransactionBinding, TransactionViewModel> {

    @Autowired(name = RouteConst.TOKEN_NAME)
    public String tokenName;

    @Override
    protected void init() {
        setAppBarView(mDataBinding.appbar);
        mDataBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDataBinding.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.startScan(TransactionActivity.this);
            }
        });

        mDataBinding.btnDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.checkAccount(mDataBinding.editTo.getText().toString().trim(),
                        mDataBinding.editAmount.getText().toString().trim(),
                        mDataBinding.editRemark.getText().toString().trim(),
                        TransactionActivity.this);
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
        mViewModel = new TransactionViewModel();
        mViewModel.setTokenName(tokenName);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_transaction;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            mDataBinding.editTo.setText(result.getContents());
        }
    }
}
