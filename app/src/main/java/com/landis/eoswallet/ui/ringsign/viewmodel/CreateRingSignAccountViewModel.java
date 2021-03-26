package com.landis.eoswallet.ui.ringsign.viewmodel;

import android.app.Activity;
import android.databinding.Bindable;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.BR;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.event.Event;
import com.landis.eoswallet.net.manage.EosAccountManger;
import com.landis.eoswallet.net.manage.EosTransferManger;
import com.landis.eoswallet.net.manage.HttpManage;
import com.landis.eoswallet.net.model.RingSignAccountInfo;
import com.landis.eoswallet.ui.wallet.viewmodel.WalletViewModel;
import com.landis.eoswallet.util.RxUtils;
import com.landis.eoswallet.util.eos.Utils;
import com.landis.eoswallet.widget.dialog.LoadDialog;
import com.landis.eoswallet.widget.dialog.PurseInputDialog;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

/**
 * 创建环签账户
 */
public class CreateRingSignAccountViewModel extends BaseViewModel {

    //bxc 余额
    @Bindable
    public BigDecimal bxcBalance;

    private LoadDialog loadDialog;

    private WeakReference<Activity> referenceActivity;

    public void init(Activity activity){
        referenceActivity=new WeakReference<>(activity);
        bxcBalance = Utils.convertBigDecimal(EosAccountManger.getInstance().getBalance("BXC"));
        notifyPropertyChanged(BR.bxcBalance);
        initLoadDialog();
    }

    private void initLoadDialog(){
        loadDialog = new LoadDialog(referenceActivity.get());
    }

    /**
     * 检查账户是否存在
     * @param accountName  账户名
     */
    public void checkAccount(String accountName) {

        if (StringUtils.isEmpty(accountName)){
            ToastUtils.showShort(R.string.ring_sign_empty);
            return;
        }

        if (accountName.length()!=7){
            ToastUtils.showShort(R.string.account_length_error);
            return;
        }

        if (bxcBalance.compareTo(Utils.convertBigDecimal("1"))==-1){
            ToastUtils.showShort(R.string.balance_insufficient);
            return;
        }
        String mulAccount="mul."+accountName;
        HttpManage.apiService.checkAccount(mulAccount).compose(RxUtils.applySchedulers()).subscribe(jsonObject -> {
            if (jsonObject.has("account_name") && !StringUtils.isEmpty(jsonObject.get("account_name").getAsString())) {
                ToastUtils.showShort(R.string.account_already_exists);
            } else {
                showPasswordDialog(mulAccount);
            }
        }, e -> {
            if (e instanceof ConnectException || e instanceof UnknownHostException || e instanceof SocketTimeoutException || e instanceof SSLHandshakeException) {
                ToastUtils.showShort(R.string.check_network_connection);
            } else {
                showPasswordDialog(mulAccount);
            }
        });
    }

    /**
     * 显示密码输入框
     * @param account
     */
    private void showPasswordDialog(String account){
        PurseInputDialog inputDialog = new PurseInputDialog(referenceActivity.get());
        inputDialog.setOnClickListener(view -> {
            String keyWord = (String) view.getTag();
            String text = SPUtils.getInstance().getString(SpConst.P_K);
            if (TextUtils.isEmpty(text)) {
                return;
            }
            WalletViewModel walletVM = new WalletViewModel();
            byte[] bytes = EncryptUtils.decryptHexStringAES(text, EncryptUtils.encryptMD5ToString(keyWord.getBytes()).getBytes(), "AES/CBC/PKCS5Padding", walletVM.hexString2Bytes(walletVM.iv));
            if (bytes == null || bytes.length == 0) {
                ToastUtils.showLong(R.string.password_error);
                return;
            }
            String priKey = new String(bytes);
            createRingSignAccount(account,priKey, SPUtils.getInstance().getString(SpConst.P_B_K).replace("BXC", "EOS"));
        });
        inputDialog.show();
    }
    /**
     * 创建账户
     * @param userName
     */
    private void createRingSignAccount(String userName, String priKey, String pubKey) {
        loadDialog.show();
        loadDialog.setTipContent(referenceActivity.get().getResources().getString(R.string.creating));
        EosTransferManger.getInstance().createAccount(userName, priKey, pubKey, (isSuccess, o) -> {
            loadDialog.dismiss();
            if (isSuccess) {
                RingSignAccountInfo ringSignAccountInfo = new RingSignAccountInfo();
                ringSignAccountInfo.accountName=userName;
                ringSignAccountInfo.showConfig=true;
                ringSignAccountInfo.save();
                EventBus.getDefault().post(new Event.RingSignAccountFollow());
                ARouter.getInstance().build(RouteConst.AV_CONFIG_RING_SIGN_AUTH).withSerializable("config_account",ringSignAccountInfo).navigation();
                ToastUtils.showShort(R.string.create_success);
                referenceActivity.get().finish();
            } else {
                ToastUtils.showShort(referenceActivity.get().getResources().getString(R.string.create_fail)+ o.toString());
            }
        });
    }
}
