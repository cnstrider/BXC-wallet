package com.landis.eoswallet.ui.me.viewmodel;

import android.databinding.Bindable;

import com.blankj.utilcode.util.SPUtils;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;

public class MeViewModel extends BaseViewModel {

    @Bindable
    public String accountName;

    public void getAccountName() {
        accountName = SPUtils.getInstance().getString(SpConst.WALLET_NAME);
    }
}
