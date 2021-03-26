package com.landis.eoswallet.ui.account.view;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.databinding.ActivityImportAccountBinding;
import com.landis.eoswallet.event.Event;
import com.landis.eoswallet.ui.account.viewmodel.ImportAccountViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 导入私钥
 */
@Route(path = RouteConst.AV_IMPORT_ACCOUNT)
public class ImportAccountActivity extends BaseActivity<ActivityImportAccountBinding, ImportAccountViewModel> {


    @Override
    protected void init() {

        mDataBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDataBinding.btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.checkAccount(mDataBinding.editKey.getText().toString().trim());
            }
        });
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void initViewModel() {
        mViewModel = new ImportAccountViewModel();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_import_account;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void createOk(Event.CreateOk ok) {
        finish();
    }
}
