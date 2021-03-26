package com.landis.eoswallet.ui.ringsign.viewmodel;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.BR;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.net.manage.HttpManage;
import com.landis.eoswallet.net.model.RingSignAccountInfo;
import com.landis.eoswallet.net.model.UserAvailableList;
import com.landis.eoswallet.util.RxUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.functions.Consumer;

public class ConfigRingSignAuthViewModel extends BaseViewModel {

    @Bindable
    public RingSignAccountInfo configAccount;
    //bxc 余额
    public Float bxcBalance;

    @Bindable
    public String strBxcBalance;

    @Bindable
    public final ObservableList<Object> authList = new ObservableArrayList<>();
    public void  setConfigAccount(RingSignAccountInfo ringSignAccountInfo){
        this.configAccount=ringSignAccountInfo;
        notifyPropertyChanged(BR.configAccount);
        getProposeAccountBalance();
    }

    /**
     * 获取联合账号余额
     */
    private void getProposeAccountBalance(){
        HttpManage.apiService.getTableRows("eosio","eosio","accounts",configAccount.accountName,20,
                true).compose(RxUtils.applySchedulers()).subscribe(new Consumer<UserAvailableList>() {
            @Override
            public void accept(UserAvailableList userAvailableList) throws Exception {
                if (null!=userAvailableList&&!userAvailableList.rows.isEmpty()){
                    bxcBalance =Float.parseFloat(userAvailableList.rows.get(0).available.replace(" EOS",""));
                    strBxcBalance = bxcBalance+" BXC";
                    notifyPropertyChanged(BR.strBxcBalance);
                }
            }
        });
    }

    /**
     * 检测账号是否存在
     * @param authAccount  权限账号
     */
    public void checkAccount(String authAccount) {
        if (authList.size()>4){
            ToastUtils.showShort(R.string.account_more);
            return;
        }

        if (StringUtils.isEmpty(authAccount)) {
            ToastUtils.showShort(R.string.weight_account_empty);
            return;
        }
        HttpManage.apiService.checkAccount(authAccount).compose(RxUtils.applySchedulers()).subscribe(jsonObject -> {
            if (!jsonObject.has("account_name") && StringUtils.isEmpty(jsonObject.get("account_name").getAsString())) {
                ToastUtils.showShort(R.string.account_not_exist);
            } else {
                ToastUtils.showShort(R.string.add_success);
                authList.add(authAccount);
                notifyPropertyChanged(BR.authList);
            }
        }, e -> {
            if (e instanceof ConnectException || e instanceof UnknownHostException || e instanceof SocketTimeoutException || e instanceof SSLHandshakeException) {
                ToastUtils.showShort(R.string.check_network_connection);
            } else {
                ToastUtils.showShort(R.string.account_not_exist);
            }
        });
    }
}
