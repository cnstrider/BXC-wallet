package com.landis.eoswallet.ui.account.viewmodel;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.enums.LoadState;
import com.landis.eoswallet.net.manage.HttpManage;
import com.landis.eoswallet.util.RxUtils;
import com.landis.eoswallet.util.eos.ec.EosPrivateKey;

public class ImportAccountViewModel extends BaseViewModel {


    public ImportAccountViewModel() {
    }

    /**
     * 检查账号是否创建
     */
    public void checkAccount(String priKey) {

        if (TextUtils.isEmpty(priKey)) {
            ToastUtils.showShort(R.string.pri_key_empty);
            return;
        }
        String pubKey = "";
        EosPrivateKey privateKey;
        try {
            privateKey = new EosPrivateKey(priKey);
        } catch (Exception e) {
            ToastUtils.showShort(R.string.pri_key_error);
            return;
        }
        if (privateKey != null && privateKey.getPublicKey() != null) {
            pubKey = privateKey.getPublicKey().toString();
        }
        if (TextUtils.isEmpty(pubKey)) {
            ToastUtils.showShort(R.string.pri_key_error);
            return;
        }

        LogUtils.d("导入私钥获取到的公钥："+pubKey);
        String finalPubKey = pubKey;
        loadStateObservableField.set(LoadState.LOADING);
        addDisposable(HttpManage.apiService.getKeyAccounts(pubKey).compose(RxUtils.applySchedulers()).subscribe(userInfo -> {
//                LogUtils.e(userInfo.accountNames);
            if (userInfo != null && userInfo.accountNames != null && userInfo.accountNames.size() > 0) {
                Log.d("getKeyAccounts Info==", userInfo.toString());

                ARouter.getInstance().build(RouteConst.AV_SET_WALLET_PASSWORD).withString("account_name", userInfo.accountNames.get(0))
                        .withString("pri_key", priKey)
                        .withString("pub_key", finalPubKey).navigation();
            }else{
                LogUtils.d("-----");
            }
        }, e -> {
            ToastUtils.showShort(e.getMessage());
        }));

    }
}
