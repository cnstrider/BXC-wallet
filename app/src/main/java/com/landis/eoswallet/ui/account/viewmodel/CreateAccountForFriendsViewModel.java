package com.landis.eoswallet.ui.account.viewmodel;

import android.app.Activity;
import android.databinding.Bindable;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.BR;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.net.manage.EosTransferManger;
import com.landis.eoswallet.net.manage.HttpManage;
import com.landis.eoswallet.ui.wallet.viewmodel.WalletViewModel;
import com.landis.eoswallet.util.CopyUtils;
import com.landis.eoswallet.util.RandomAccountUtils;
import com.landis.eoswallet.util.RxUtils;
import com.landis.eoswallet.widget.LoadingProgress;
import com.landis.eoswallet.widget.dialog.PurseInputDialog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

public class CreateAccountForFriendsViewModel extends BaseViewModel {

    //公钥
    @Bindable
    public String pubKey;

    //随机账户名
    @Bindable
    public String randomAccount;

    public void setPubKey(String publicKey) {
        this.pubKey = publicKey;
        notifyPropertyChanged(BR.pubKey);
    }

    /**
     * 创建随机账户名
     */
    public void createRandomAccount() {
        randomAccount = RandomAccountUtils.getRandomAccount();
        notifyPropertyChanged(BR.randomAccount);
    }

    public void copyPubKey(View view) {
        CopyUtils.copy(view.getContext(), "pub_key", pubKey);
        ToastUtils.showLong(R.string.copy_account_pub_key_success);
    }

    /**
     * 验证密码
     *
     * @param activity
     */
    public void verifyPassword(Activity activity) {
        PurseInputDialog inputDialog = new PurseInputDialog(activity);
        inputDialog.setOnClickListener(view -> {
            String key = (String) view.getTag();
            String text = SPUtils.getInstance().getString(SpConst.P_K);
            if (TextUtils.isEmpty(text)) {
                return;
            }
            WalletViewModel walletVM = new WalletViewModel();
            byte[] bytes = EncryptUtils.decryptHexStringAES(text, EncryptUtils.encryptMD5ToString(key.getBytes()).getBytes(), "AES/CBC/PKCS5Padding", walletVM.hexString2Bytes(walletVM.iv));
            if (bytes == null || bytes.length == 0) {
                ToastUtils.showLong(R.string.password_error);
                return;
            }
            String decryptPrivateKey = new String(bytes);
            checkRandomAccount(activity, decryptPrivateKey);
        });
        inputDialog.show();
    }

    /**
     * 检测账号是否存在
     *
     * @param decryptPrivateKey
     */
    private void checkRandomAccount(Activity context, String decryptPrivateKey) {
        LoadingProgress.show(context, false);
        HttpManage.apiService.checkAccount(randomAccount).compose(RxUtils.applySchedulers()).subscribe(jsonObject -> {
            if (jsonObject.has("account_name") && !StringUtils.isEmpty(jsonObject.get("account_name").getAsString())) {
                createRandomAccount();
                checkRandomAccount(context, decryptPrivateKey);
            } else {
                createAccount(decryptPrivateKey,context);
            }
        }, e -> {
            LoadingProgress.dismissProgress();
            if (e instanceof ConnectException || e instanceof UnknownHostException || e instanceof SocketTimeoutException || e instanceof SSLHandshakeException) {
                ToastUtils.showShort(R.string.check_network_connection);
            } else {
                createAccount(decryptPrivateKey,context);
            }
        });
    }

    /**
     * 创建账号
     *
     * @param decryptPrivateKey 解密后的私钥
     */
    private void createAccount(String decryptPrivateKey, Activity activity) {
        EosTransferManger.getInstance().createAccount(randomAccount, decryptPrivateKey, pubKey, (isSuccess, o) -> {
            LoadingProgress.dismissProgress();
            if (isSuccess) {
                ToastUtils.showShort(R.string.create_success);
                activity.finish();
            } else {
                ToastUtils.showShort(o.toString());
            }
        });
    }
}
