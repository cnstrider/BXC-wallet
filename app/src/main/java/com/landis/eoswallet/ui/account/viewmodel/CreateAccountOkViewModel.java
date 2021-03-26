package com.landis.eoswallet.ui.account.viewmodel;

import com.landis.eoswallet.base.viewmodel.BaseViewModel;

/**
 * 账号创建完成viewModel
 */
public class CreateAccountOkViewModel extends BaseViewModel {

    public String pubKey;
    public String priKey;
    public String accountName;

    public CreateAccountOkViewModel(String pubKey, String priKey, String accountName) {
        this.pubKey = pubKey;
        this.priKey = priKey;
        this.accountName = accountName;
        notifyChange();
    }

}
