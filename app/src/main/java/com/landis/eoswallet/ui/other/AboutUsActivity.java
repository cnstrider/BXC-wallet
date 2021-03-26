package com.landis.eoswallet.ui.other;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.landis.eoswallet.BuildConfig;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.databinding.ActivityAboutUsBinding;
import com.landis.eoswallet.util.eos.Utils;

@Route(path = RouteConst.AV_ABOUT_US)
public class AboutUsActivity extends BaseActivity<ActivityAboutUsBinding, BaseViewModel> {
    @Override
    protected void init() {
        mDataBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDataBinding.tvAppName.setText("BXC V"+ BuildConfig.VERSION_NAME);
        mDataBinding.tvWebsite.setText("Copyright "+ Utils.getYear()+" LosPocket BXC Team");
    }

    @Override
    protected void bindViewModel() {

    }

    @Override
    protected void initViewModel() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_about_us;
    }
}
