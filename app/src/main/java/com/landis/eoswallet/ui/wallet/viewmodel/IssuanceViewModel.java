package com.landis.eoswallet.ui.wallet.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.text.TextUtils;
import android.widget.EditText;

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
import com.landis.eoswallet.util.eos.Utils;
import com.landis.eoswallet.widget.dialog.PurseInputDialog;

import org.greenrobot.eventbus.EventBus;

public class IssuanceViewModel extends BaseViewModel {

    @Bindable
    public String bxcBalance;

    public String mIssuanceNumber = "";

    //添加分隔符的代币数量
    @Bindable
    public String mIssuanceNumberComma ;

    public void getBxcBalance() {
        bxcBalance = EosAccountManger.getInstance().getBalance("BXC");
        notifyPropertyChanged(BR.bxcBalance);
    }

    /**
     * 发行代币
     * @param tokenName 代币名称
     * @param issuanceNumber 代币数量
     */
    public void issuanceToken(String tokenName, String issuanceNumber, Context context){
        if (StringUtils.isEmpty(tokenName)) {
            ToastUtils.showShort("请输入代币名称");
            return;
        }

        if (tokenName.length() < 2
                || tokenName.length() > 6) {
            ToastUtils.showShort("代币名称请输入2~6个字符");
            return;
        }
        if (StringUtils.isEmpty(issuanceNumber)) {
            ToastUtils.showShort("请输入代币发行量");
            return;
        }
        if (Long.valueOf(issuanceNumber.replace(",", "")) == 0
                || Long.valueOf(issuanceNumber.replace(",", "")) > 10000000000L) {
            ToastUtils.showShort("发行量不能为0或大于10000000000枚");
            return;
        }
        if (Float.valueOf(bxcBalance) < 600f) {
            ToastUtils.showShort("BXC余额不足");
            return;
        }

        PurseInputDialog inputDialog = new PurseInputDialog(context);
        inputDialog.setOnClickListener(view -> {
            String key = (String) view.getTag();
            String text = SPUtils.getInstance().getString(SpConst.P_K);
            if (TextUtils.isEmpty(text)) {
                return;
            }
            WalletViewModel walletVM = new WalletViewModel();
            byte[] bytes = EncryptUtils.decryptHexStringAES(text, EncryptUtils.encryptMD5ToString(key.getBytes()).getBytes(), "AES/CBC/PKCS5Padding", walletVM.hexString2Bytes(walletVM.iv));
            if (bytes == null || bytes.length == 0) {
                ToastUtils.showLong("密码错误");
                return;
            }
            String pri = new String(bytes);
            EosTransferManger.getInstance().issueCurrency(tokenName,
                    Utils.formatFour(Double.valueOf(issuanceNumber.replace(",", ""))) +
                            " " + tokenName, pri, new EosTransferManger.Callback() {
                        @Override
                        public void onback(boolean isSuccess, Object o) {
                            if (isSuccess) {
                                ToastUtils.showShort("发行成功");
                                EventBus.getDefault().post(new Event.IssuanceTokenSuccess());
                            } else {
                                ToastUtils.showShort("发行失败" + o.toString());
                            }
                        }
                    });
        });
        inputDialog.show();
    }

    /**
     * 添加分位符
     * @param str
     * @param edtext
     * @return
     */
    public void addComma(String str, EditText edtext) {
        mIssuanceNumber = edtext.getText().toString().trim().replaceAll(",", "");

        StringBuilder sb = new StringBuilder(str);
        sb.reverse();
        for (int i = 3; i < sb.length(); i += 4) {
            sb.insert(i, ',');
        }
        sb.reverse();
        mIssuanceNumberComma= sb.toString();
        notifyPropertyChanged(BR.issuanceNumberComma);
        edtext.setSelection(mIssuanceNumber.length());
    }
}
