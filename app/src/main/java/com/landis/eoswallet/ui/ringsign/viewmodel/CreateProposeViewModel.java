package com.landis.eoswallet.ui.ringsign.viewmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.databinding.Bindable;
import android.text.TextUtils;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.BR;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.event.Event;
import com.landis.eoswallet.net.manage.EosAccountManger;
import com.landis.eoswallet.net.manage.EosTransferManger;
import com.landis.eoswallet.net.manage.HttpManage;
import com.landis.eoswallet.net.model.UserAvailableList;
import com.landis.eoswallet.net.model.UserTokenAvailableList;
import com.landis.eoswallet.ui.wallet.viewmodel.WalletViewModel;
import com.landis.eoswallet.util.RxUtils;
import com.landis.eoswallet.util.eos.Utils;
import com.landis.eoswallet.widget.dialog.LoadDialog;
import com.landis.eoswallet.widget.dialog.PurseInputDialog;
import com.landis.eoswallet.widget.dialog.SelectTokenDialog;

import org.greenrobot.eventbus.EventBus;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.functions.Consumer;

public class CreateProposeViewModel extends BaseViewModel {

    //bxc 余额
    public Float bxcBalance;

    public Float proposeAccountBalance = 0F;
    private SelectTokenDialog selectTokenDialog;

    public String proposeAccount;

    //转账代币
    @Bindable
    public String tokenName;
    //转账金额
    private String mProposalTransferAmount;

    private Activity mContext;

    private LoadDialog loadDialog;

    public void setContext(Activity context) {
        this.mContext = context;
    }

    /**
     * 设置环签账户名
     *
     * @param proposeAccount
     */
    public void setProposeAccount(String proposeAccount) {
        this.proposeAccount = proposeAccount;
    }

    /**
     * 获取BXC余额
     */
    public void getBxcBalance() {
        bxcBalance = Float.valueOf(EosAccountManger.getInstance().getBalance("BXC"));
    }

    /**
     * 显示选择币种dialog
     */
    public void showSelectTokenDialog() {
        if (null == selectTokenDialog) {
            selectTokenDialog = new SelectTokenDialog(mContext);
            selectTokenDialog.setOnItemClickListener(new SelectTokenDialog.OnItemClickListener() {
                @Override
                public void onItemClick(String selectToken) {
                    if (!StringUtils.isEmpty(selectToken)) {
                        tokenName = selectToken;

                        if (TextUtils.equals("BXC", selectToken)) {
                            getProposeAccountBalance();
                        } else {
                            getProposeAccountTokenBalance();
                        }
                        notifyPropertyChanged(BR.tokenName);
                    }
                }
            });
        }
        selectTokenDialog.show();
    }

    /**
     * 获取联合账号余额
     */
    public void getProposeAccountBalance() {

        HttpManage.apiService.getTableRows("eosio", "eosio", "accounts", proposeAccount, 20,
                true).compose(RxUtils.applySchedulers()).subscribe(new Consumer<UserAvailableList>() {
            @Override
            public void accept(UserAvailableList userAvailableList) throws Exception {
                if (null != userAvailableList && !userAvailableList.rows.isEmpty()) {
                    proposeAccountBalance =
                            Float.valueOf(userAvailableList.rows.get(0).available.replace(" EOS",
                                    ""));
                }
            }
        });
    }

    /**
     * 获取联合账号Token余额
     */
    public void getProposeAccountTokenBalance() {
        proposeAccountBalance = 0.0000F;
        HttpManage.apiService.getTokenBalance(proposeAccount, "eosio.token", "accounts",
                "", 20, true).compose(RxUtils.applySchedulers()).subscribe(new Consumer<UserTokenAvailableList>() {
            @Override
            public void accept(UserTokenAvailableList userTokenAvailableList) throws Exception {
                if (null != userTokenAvailableList && !userTokenAvailableList.rows.isEmpty()) {
                    for (UserTokenAvailableList.TokenAvailable tokenAvailable :
                            userTokenAvailableList.rows) {
                        String[] splitBalance = tokenAvailable.balance.split(
                                " ");
                        if (splitBalance[1].equals(tokenName)) {
                            proposeAccountBalance = Float.valueOf(splitBalance[0]);
                        }
                    }
                }
            }
        }, e -> {
            proposeAccountBalance = 0.0000F;
        });
    }

    @SuppressLint("CheckResult")
    public void createPropose(String proposeName, String proposalTransferAccount,
                              String proposalTransferAmount, String memo) {
        this.mProposalTransferAmount = proposalTransferAmount;
        if (bxcBalance < 1.5F) {
            ToastUtils.showShort("手续费余额不足");
            return;
        }

        if (StringUtils.isEmpty(proposeName)) {
            ToastUtils.showShort("请输入提案名称");
            return;
        }
        if (StringUtils.isEmpty(proposalTransferAccount)) {
            ToastUtils.showShort("请输入转入账户");
            return;
        }
        if (StringUtils.isEmpty(proposalTransferAmount)) {
            ToastUtils.showShort("请输入转入金额");
            return;
        }

        if (Float.valueOf(proposalTransferAmount) == 0) {
            ToastUtils.showShort("转入金额不能为0");
            return;
        }

        if ((proposeAccountBalance - Float.valueOf(mProposalTransferAmount)) < 0) {
            ToastUtils.showShort("联名账户余额不足");
            return;
        }

        HttpManage.apiService.checkAccount(proposalTransferAccount).compose(RxUtils.applySchedulers()).subscribe(jsonObject -> {
            if (jsonObject.has("account_name") && !StringUtils.isEmpty(jsonObject.get(
                    "account_name").getAsString())) {
                PurseInputDialog inputDialog = new PurseInputDialog(mContext);
                inputDialog.setOnClickListener(view -> {
                    String key = (String) view.getTag();
                    String text = SPUtils.getInstance().getString(SpConst.P_K);
                    if (TextUtils.isEmpty(text)) {
                        return;
                    }
                    WalletViewModel walletViewModel = new WalletViewModel();
                    byte[] bytes = EncryptUtils.decryptHexStringAES(text,
                            EncryptUtils.encryptMD5ToString(key.getBytes()).getBytes(), "AES/CBC" +
                                    "/PKCS5Padding",
                            walletViewModel.hexString2Bytes(walletViewModel.iv));
                    if (bytes == null || bytes.length == 0) {
                        ToastUtils.showShort("密码错误");
                        return;
                    }
                    String pri = new String(bytes);
                    if (null==loadDialog){
                        loadDialog=new LoadDialog(mContext);
                    }
                    loadDialog.show();
                    loadDialog.setTipContent("正在提交");
                    //多签提案测试
                    String action = "";
                    if (tokenName.equals("BXC")) {
                        mProposalTransferAmount =
                                Utils.formatEosBalance(Double.parseDouble(mProposalTransferAmount)) + " EOS";
                        action = "eosio";
                    } else {
                        mProposalTransferAmount =
                                Utils.formatEosBalance(Double.parseDouble(mProposalTransferAmount)) + " " + tokenName;
                        action = "eosio.token";
                    }
                    EosTransferManger.getInstance().multipleSignatures(pri, proposeName,
                            mProposalTransferAmount, action, proposeAccount,
                            proposalTransferAccount, memo, (isSuccess, o) -> {
                        loadDialog.dismiss();
                        if (isSuccess) {
                            ToastUtils.showShort("提案成功");
                            EventBus.getDefault().post(new Event.RefreshPropose());
                        } else {
                            ToastUtils.showShort("提案失败：" + o.toString());
                        }
                    });

                });
                inputDialog.show();
            } else {
                loadDialog.dismiss();
                ToastUtils.showShort("请检查转入账号是否正确");
            }
        }, e -> {
            if (e instanceof ConnectException || e instanceof UnknownHostException || e instanceof SocketTimeoutException || e instanceof SSLHandshakeException) {
                ToastUtils.showShort("请检查网络连接");
            } else {
                ToastUtils.showShort("请检查转入账号是否正确");
            }
        });
    }
}
