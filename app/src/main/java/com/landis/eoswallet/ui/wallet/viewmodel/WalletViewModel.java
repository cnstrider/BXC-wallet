package com.landis.eoswallet.ui.wallet.viewmodel;

import android.databinding.Bindable;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.util.eos.ec.EosPrivateKey;

import java.util.ArrayList;
import java.util.Random;

public class WalletViewModel extends BaseViewModel {

    public String accountName;

    public String iv = "42C9B2AC14DF14BD7A90040683556907";
    public String priKey;
    public String pubKey;

    @Bindable
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
//        notifyPropertyChanged(BR.userName);
    }

    public void randomUserName() {
        Random random = new Random();
        String str = String.valueOf((char) (97 + random.nextInt(26)));
        for (int i = 1; i < 11; i++) {
            boolean b = random.nextBoolean();
            if (b) {
                str += (char) (97 + random.nextInt(26));
            } else {
                str += String.valueOf(1 + random.nextInt(5));
            }
        }
        setAccountName(str);
    }

    public void getPriPubKey() {
        EosPrivateKey ownerKey = getPrivateKey(1).get(0);
        priKey = ownerKey.toString();
        pubKey = ownerKey.getPublicKey().toString();
    }

    private ArrayList<EosPrivateKey> getPrivateKey(int count) {
        ArrayList<EosPrivateKey> keys = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            keys.add(new EosPrivateKey());
        }
        return keys;
    }

    public static byte[] hexString2Bytes(String hexString) {
        if (isSpace(hexString)) return null;
        int len = hexString.length();
        if (len % 2 != 0) {
            hexString = "0" + hexString;
            len = len + 1;
        }
        char[] hexBytes = hexString.toUpperCase().toCharArray();
        byte[] ret = new byte[len >> 1];
        for (int i = 0; i < len; i += 2) {
            ret[i >> 1] = (byte) (hex2Dec(hexBytes[i]) << 4 | hex2Dec(hexBytes[i + 1]));
        }
        return ret;
    }

    private static int hex2Dec(final char hexChar) {
        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - '0';
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 'A' + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
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

        keepPri(password);
        SPUtils.getInstance().put(SpConst.WALLET_NAME, accountName, true);
        String strPubKey = pubKey;
        strPubKey = strPubKey.replace("EOS", "BXC");

        ARouter.getInstance().build(RouteConst.AV_CREATE_ACCOUNT_OK).withString
                ("account_name", accountName)
                .withString("pri_key", priKey)
                .withString("pub_key", strPubKey).navigation();
        ToastUtils.showShort(R.string.create_success);
    }


    public void keepPri(String key) {
        String strPubKey = pubKey;
        strPubKey = strPubKey.replace("EOS", "BXC");
        SPUtils.getInstance().put(SpConst.P_B_K, strPubKey, true);
        String ppKey = EncryptUtils.encryptAES2HexString(priKey.getBytes(), EncryptUtils.encryptMD5ToString(key.getBytes()).getBytes(), "AES/CBC/PKCS5Padding", hexString2Bytes(iv));
        SPUtils.getInstance().put(SpConst.P_K, ppKey, true);
//        String text = SPUtils.getInstance().getString(SpConst.P_K);

    }
}
