package com.landis.eoswallet.ui.ringsign.viewmodel;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.BR;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.net.manage.HttpManage;
import com.landis.eoswallet.net.model.EosAccountInfo;
import com.landis.eoswallet.net.model.WeightInfo;
import com.landis.eoswallet.util.RxUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class WeightsDetailsViewModel extends BaseViewModel {

    public final ObservableList<Object> weightList = new ObservableArrayList<>();

    @Bindable
    public int countWeights;

    /**
     * 查询权重
     *
     * @param accountName
     */
    public void queryWeights(String accountName) {

        List<WeightInfo> list = new ArrayList<>();

        HttpManage.apiService.checkJointlyAccount(accountName).compose(RxUtils.applySchedulers()).subscribe(new Consumer<EosAccountInfo>() {
            @Override
            public void accept(EosAccountInfo eosAccountInfo) throws Exception {
                if (!eosAccountInfo.permissions.isEmpty()) {
                    for (int i = 0; i < eosAccountInfo.permissions.size(); i++) {
                        if (!eosAccountInfo.permissions.get(i).required_auth.accounts.isEmpty()) {
                            for (int j = 0; j < eosAccountInfo.permissions.get(i).required_auth.accounts.size(); j++) {
                                WeightInfo weightInfo = new WeightInfo();
                                weightInfo.actor=eosAccountInfo.permissions.get(i).required_auth.accounts.get(j).permission.actor;
                                weightInfo.permission=eosAccountInfo.permissions.get(i).required_auth.accounts.get(j).permission.permission;
                                weightInfo.weight=eosAccountInfo.permissions.get(i).required_auth.accounts.get(j).weight;
                                list.add(weightInfo);
                            }
                            countWeights =
                                    eosAccountInfo.permissions.get(i).required_auth.threshold;
                            notifyPropertyChanged(BR.countWeights);
                        }
                    }
                    weightList.addAll(list);
                }
            }
        },e->{
            ToastUtils.showShort(e.getMessage());
        });
    }
}
