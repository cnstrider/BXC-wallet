package com.landis.eoswallet.ui.wallet.viewmodel;

import android.app.Activity;
import android.databinding.Bindable;
import android.text.TextUtils;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.landis.eoswallet.BR;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.net.manage.EosAccountManger;
import com.landis.eoswallet.net.manage.EosTransferManger;
import com.landis.eoswallet.net.manage.HttpManage;
import com.landis.eoswallet.ui.other.view.ScanActivity;
import com.landis.eoswallet.util.RxUtils;
import com.landis.eoswallet.widget.LoadingProgress;
import com.landis.eoswallet.widget.dialog.PurseInputDialog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;

import javax.net.ssl.SSLHandshakeException;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class TransactionViewModel extends BaseViewModel {

    //币种名称
    @Bindable
    public String tokenName;

    private PurseInputDialog inputDialog;

    public String tokenBalance;

    //bxc余额
    public float bxcBalance;

    public void setTokenName(String name){
        this.tokenName=name;
        notifyPropertyChanged(BR.tokenName);
        tokenBalance=EosAccountManger.getInstance().getBalance(tokenName);
        bxcBalance= Float.valueOf(EosAccountManger.getInstance().getBalance("BXC"));
    }


    public void startScan(Activity activity) {
        new IntentIntegrator(activity).setCaptureActivity(ScanActivity.class)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                .setRequestCode(REQUEST_CODE)
                .setPrompt("")
                .setCameraId(0)
                .setBeepEnabled(true)
                .setBarcodeImageEnabled(false)
                .initiateScan();
    }


    /**
     * 检查账户是否存在
     *
     * @param toAccount 收款账户
     */
    public void checkAccount(String toAccount, String amount, String memo, Activity activity) {
        HttpManage.apiService.checkAccount(toAccount).compose(RxUtils.applySchedulers()).subscribe(jsonObject -> {
            if (jsonObject.has("account_name") && !StringUtils.isEmpty(jsonObject.get("account_name").getAsString())) {
                transfer(toAccount, amount, memo, activity);
            } else {
                ToastUtils.showShort("账户不存在，请重新输入");
            }
        }, e -> {

            if (e instanceof ConnectException || e instanceof UnknownHostException || e instanceof SocketTimeoutException || e instanceof SSLHandshakeException) {
                ToastUtils.showShort("请检查网络连接");
            } else {
                ToastUtils.showShort("账户不存在，请重新输入");
            }
        });
    }

    /**
     * 转账
     */
    private void transfer(String toAccount, String amount, String memo, Activity activity) {
        if (TextUtils.isEmpty(toAccount)) {
            ToastUtils.showShort(R.string.collection_account_empty);
            return;
        }

        if (TextUtils.isEmpty(amount)) {
            ToastUtils.showShort(R.string.transaction_quantity_empty);
            return;
        }
        if (0 == Float.valueOf(amount)) {
            ToastUtils.showShort(R.string.transaction_quantity_zero);
            return;
        }

        if ((bxcBalance - 0.01) < 0) {
            ToastUtils.showShort("BXC" + activity.getResources().getString(R.string.balance_insufficient));
            return;
        }

        if ((Float.valueOf(amount) - Float.valueOf(tokenBalance)) > 0) {
            ToastUtils.showShort(R.string.balance_insufficient);
            return;
        }
        amount = formatAmount(amount) + " " + tokenName;

        String action = "eosio";
        if (!TextUtils.equals(tokenName, "BXC") && !TextUtils.equals(tokenName, "EOS")) {
            action = "eosio.token";
        }

        if (inputDialog == null) {
            inputDialog = new PurseInputDialog(activity);
        }
        inputDialog.setData(action, memo, toAccount, amount, v -> {
            String key = (String) v.getTag();
            String text = SPUtils.getInstance().getString(SpConst.P_K);
            WalletViewModel walletVM = new WalletViewModel();
            byte[] bytes = EncryptUtils.decryptHexStringAES(text, EncryptUtils.encryptMD5ToString(key.getBytes()).getBytes(), "AES/CBC/PKCS5Padding", walletVM.hexString2Bytes(walletVM.iv));
            if (bytes == null || bytes.length == 0) {
                ToastUtils.showLong(R.string.password_error);
                return;
            }
            String pri = new String(bytes);
//              ToastUtils.showShort(pri);
            LoadingProgress.show(activity, false);
            EosTransferManger.getInstance().pay(pri, inputDialog.action, inputDialog.remark, inputDialog.to, inputDialog.quantity, SPUtils.getInstance().getString(SpConst.WALLET_NAME), (isSuccess, o) -> {
                LoadingProgress.dismissProgress();
                if (isSuccess) {
                    String info = (String) o;
                    String[] infos = info.split("time=");
                    ToastUtils.showLong("交易成功! transaction_id=" + infos[0]);
//                        String balance = EosAccountManger.getInstance().getBalance(tokenName);
//                        if(!TextUtils.equals(balance, "获取不到")){
//                            serverCallback(SPUtils.getInstance().getString(SpConst.WALLET_NAME), balance, currency, SPUtils.getInstance().getString(SpConst.WALLET_NAME), to, quantity.split(" ")[0], (String) o, remark);
//                        }
//                        serverCallback(SPUtils.getInstance().getString(SpConst.WALLET_NAME), EosAccountManger.getInstance().getBalance(tokenName), tokenName, SPUtils.getInstance().getString(SpConst.WALLET_NAME), inputDialog.to,
//                                binding.editAmount.getText().toString(), (String) o, remark, infos[1]);
//                        onBackPressed();
                    activity.finish();

                } else {
                    ToastUtils.showLong(activity.getResources().getString(R.string.transaction_fail) + o.toString());
                }
            });
        });
        if (!inputDialog.isShowing())
            inputDialog.show();
    }

    /**
     * 格式化金额
     *
     * @param amount
     * @return
     */
    private String formatAmount(String amount) {
        Double d = Double.valueOf(amount);
        DecimalFormat nf = new DecimalFormat("##0.0000");
        nf.setMaximumFractionDigits(4);
        return nf.format(d);
    }

}
