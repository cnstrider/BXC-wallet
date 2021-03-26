package com.landis.eoswallet.ui.wallet.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.databinding.ActivityIssuanceBinding;
import com.landis.eoswallet.event.Event;
import com.landis.eoswallet.ui.wallet.viewmodel.IssuanceViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 一键发币
 */
@Route(path = RouteConst.AV_ISSUANCE)
public class IssuanceActivity extends BaseActivity<ActivityIssuanceBinding, IssuanceViewModel> {
    @Override
    protected void init() {
        setAppBarView(mDataBinding.appbar);
        mDataBinding.ivIssuanceBack.setOnClickListener(v -> finish());
        mViewModel.getBxcBalance();
        mDataBinding.edIssuanceNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mViewModel.mIssuanceNumber.equals(mDataBinding.edIssuanceNumber.getText().toString().trim().replaceAll(",", ""))) {
                    mViewModel.addComma(mDataBinding.edIssuanceNumber.getText().toString().trim().replaceAll(",", ""), mDataBinding.edIssuanceNumber);
                }
            }
        });

        mDataBinding.tvIssuanceContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tokenName = mDataBinding.edIssuanceTokenName.getText().toString().trim().toUpperCase();
                String issuanceNumber = mDataBinding.edIssuanceNumber.getText().toString().trim();
                mViewModel.issuanceToken(tokenName,issuanceNumber,IssuanceActivity.this.getApplicationContext());
            }
        });
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void initViewModel() {
        mViewModel = new IssuanceViewModel();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_issuance;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void IssuanceTokenSuccess(Event.IssuanceTokenSuccess issuanceTokenSuccess) {
        finish();
    }
}
