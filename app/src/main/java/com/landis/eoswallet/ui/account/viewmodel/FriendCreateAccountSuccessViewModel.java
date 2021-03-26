package com.landis.eoswallet.ui.account.viewmodel;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.ui.wallet.viewmodel.WalletViewModel;
import com.landis.eoswallet.util.CopyUtils;

public class FriendCreateAccountSuccessViewModel extends BaseViewModel {

    private WalletViewModel walletViewModel;


    public String pubKey;
    public String priKey;
    public String accountName;

    public FriendCreateAccountSuccessViewModel(String pubKey, String priKey, String accountName) {
        this.pubKey = pubKey;
        this.priKey = priKey;
        this.accountName = accountName;
        walletViewModel=new WalletViewModel();
        walletViewModel.pubKey=pubKey;
        walletViewModel.priKey=priKey;
        notifyChange();
    }

    /**
     * 复制公钥
     *
     * @param context
     */
    public void copyPubKey(Context context) {
        CopyUtils.copy(context, "pub_key", pubKey);
        ToastUtils.showLong(R.string.copy_account_pub_key_success);
    }

    /**
     * 复制私钥
     *
     * @param context
     */
    public void copyPriKey(Context context) {
        CopyUtils.copy(context, "pri_key", priKey);
        ToastUtils.showLong(R.string.copy_account_pri_key_success);
    }

    /**
     * 保存密码
     * @param password
     */
    public void savePassword(String password,String completePassword){
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showShort(R.string.password_not_empty);
            return;
        }
        if (password.length() != 6) {
            ToastUtils.showShort(R.string.please_enter_six_digit_password);
            return;
        }
        if (TextUtils.isEmpty(completePassword)) {
            ToastUtils.showShort(R.string.commit_password_not_empty);
            return;
        }
        if (completePassword.length() != 6) {
            ToastUtils.showShort(R.string.please_enter_six_digit_commit_password);
            return;
        }
        if (!password.equals(completePassword)) {
            ToastUtils.showShort(R.string.two_password_inputs_are_inconsistent);
            return;
        }

        walletViewModel.keepPri(password);
        SPUtils.getInstance().put(SpConst.WALLET_NAME, accountName, true);
        String strPubKey = walletViewModel.pubKey;
        strPubKey = strPubKey.replace("EOS", "BXC");

        ARouter.getInstance().build(RouteConst.AV_CREATE_ACCOUNT_OK).withString
        ("account_name", accountName)
                            .withString("pri_key", priKey)
                            .withString("pub_key", strPubKey).navigation();
        ToastUtils.showShort(R.string.create_success);
    }
}
