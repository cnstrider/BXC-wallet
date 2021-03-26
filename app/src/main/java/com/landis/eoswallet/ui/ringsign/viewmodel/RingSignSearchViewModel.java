package com.landis.eoswallet.ui.ringsign.viewmodel;

import android.databinding.Bindable;

import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.BR;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.event.Event;
import com.landis.eoswallet.net.manage.HttpManage;
import com.landis.eoswallet.net.model.EosAccountInfo;
import com.landis.eoswallet.net.model.RingSignAccountInfo;
import com.landis.eoswallet.util.RxUtils;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.List;

import io.reactivex.functions.Consumer;

public class RingSignSearchViewModel extends BaseViewModel {

    @Bindable
    public String accountName;

    /**
     * 关注环签账号
     */
    public void followRingSignAccount() {
        RingSignAccountInfo ringSignAccountInfo = new RingSignAccountInfo();
        ringSignAccountInfo.accountName = accountName;
        ringSignAccountInfo.state = 0;
        upFollowData(ringSignAccountInfo);
        EventBus.getDefault().post(new Event.RingSignAccountFollow());
        ToastUtils.showShort(R.string.care_success);
    }

    /**
     * 更新数据
     *
     * @param ringSignAccountInfo
     */
    private void upFollowData(RingSignAccountInfo ringSignAccountInfo) {
        List<RingSignAccountInfo> all = LitePal.findAll(RingSignAccountInfo.class);
        for (RingSignAccountInfo accountInfo : all) {
            if (accountInfo.accountName.equals(ringSignAccountInfo.accountName)) {
                accountInfo.delete();
                ringSignAccountInfo.save();
                return;
            }
        }
        ringSignAccountInfo.save();
    }

    /**
     * 查询账号  是否是环签账号
     *
     * @param searchAccountName 搜索账号
     */
    public void searchRingSignAccount(String searchAccountName) {
        HttpManage.apiService.checkJointlyAccount(searchAccountName).compose(RxUtils.applySchedulers()).subscribe(new Consumer<EosAccountInfo>() {
            @Override
            public void accept(EosAccountInfo eosAccountInfo){
                if (!eosAccountInfo.permissions.isEmpty()) {
                    for (int i = 0; i < eosAccountInfo.permissions.size(); i++) {
                        if (eosAccountInfo.permissions.get(i).required_auth.threshold > 1) {
                            accountName = searchAccountName;
                            notifyPropertyChanged(BR.accountName);
                            return;
                        }
                    }
                }
                accountName = "";
                notifyPropertyChanged(BR.accountName);
                ToastUtils.showShort(R.string.ring_sing_account_error);
            }
        }, e -> {
            accountName = "";
            notifyPropertyChanged(BR.accountName);
            ToastUtils.showShort(R.string.ring_sing_account_error);
        });
    }
}
