package com.landis.eoswallet.ui.account.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.enums.LoadState;
import com.landis.eoswallet.net.manage.HttpManage;
import com.landis.eoswallet.net.model.BaseUserInfo;
import com.landis.eoswallet.ui.wallet.viewmodel.WalletViewModel;
import com.landis.eoswallet.util.CopyUtils;
import com.landis.eoswallet.util.RxUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.functions.Consumer;

public class FriendCreateAccountViewModel extends BaseViewModel {

    private WalletViewModel walletViewModel;


    public String pubKey;
    public String priKey;

    public FriendCreateAccountViewModel() {
        walletViewModel = new WalletViewModel();
        if (!StringUtils.isEmpty(SPUtils.getInstance().getString(SpConst.P_B_K)) && !StringUtils.isEmpty(SPUtils.getInstance().getString(SpConst.P_K))
                && StringUtils.isEmpty(SPUtils.getInstance().getString(SpConst.WALLET_NAME))) {
            walletViewModel.priKey = SPUtils.getInstance().getString(SpConst.P_K);
            walletViewModel.pubKey = SPUtils.getInstance().getString(SpConst.P_B_K);
        } else {
            walletViewModel.getPriPubKey();
            SPUtils.getInstance().put(SpConst.P_B_K, walletViewModel.pubKey);
            SPUtils.getInstance().put(SpConst.P_K, walletViewModel.priKey);
        }
        pubKey = walletViewModel.pubKey;
        priKey = walletViewModel.priKey;
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
     * 检查账号是否创建
     */
    public void checkAccount() {
        loadStateObservableField.set(LoadState.LOADING);
        LogUtils.d("公钥："+pubKey);
        addDisposable(HttpManage.apiService.getKeyAccounts(pubKey).compose(RxUtils.applySchedulers()).subscribe(new Consumer<BaseUserInfo>() {
            @Override
            public void accept(BaseUserInfo baseUserInfo) throws Exception {
                if (null != baseUserInfo && null != baseUserInfo.accountNames && !baseUserInfo.accountNames.isEmpty()) {
                    //设置钱包密码
                    ARouter.getInstance().build(RouteConst.AV_FRIEND_CREATE_ACCOUNT_SUCCESS).withString("account_name", baseUserInfo.accountNames.get(0))
                            .withString("pri_key", priKey)
                            .withString("pub_key", pubKey).navigation();
                } else {
                    ToastUtils.showShort(R.string.account_not_created_successfully);
                }
            }
        }, e -> {
            if (e instanceof ConnectException || e instanceof UnknownHostException || e instanceof SocketTimeoutException || e instanceof SSLHandshakeException) {
                ToastUtils.showShort(R.string.check_network_connection);
            } else {
                ToastUtils.showShort(e.getMessage());
            }
        }));
    }

    /**
     * 保存二维码图片
     *
     * @param bitmap
     */
    public void saveQr(Context context, Bitmap bitmap) {
        AndPermission.with(context).runtime().permission(Permission.Group.STORAGE).onGranted(new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "", "");
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                ToastUtils.showShort(R.string.saved_system_album);
            }
        }).onDenied(data -> {

        }).start();
    }
}
