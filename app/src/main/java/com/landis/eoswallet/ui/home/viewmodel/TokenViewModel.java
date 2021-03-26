package com.landis.eoswallet.ui.home.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.reflect.TypeToken;
import com.landis.eoswallet.BR;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.enums.LoadState;
import com.landis.eoswallet.enums.RefreshState;
import com.landis.eoswallet.net.manage.HttpManage;
import com.landis.eoswallet.net.model.AppTokenInfo;
import com.landis.eoswallet.util.GsonUtils;
import com.landis.eoswallet.util.RxUtils;

import java.text.DecimalFormat;
import java.util.List;

public class TokenViewModel extends BaseViewModel {

    //2位小数
    private DecimalFormat df = new DecimalFormat("0.00");

    @Bindable
    public String accountName;
    @Bindable
    public String remainingMemory;

    public final ObservableList<Object> appTokenInfoObservableList = new ObservableArrayList<>();

    public void setAccountName(String accountName){
        this.accountName=accountName;
        notifyPropertyChanged(BR.accountName);
    }

    public TokenViewModel() {
    }

    //获取账号内存信息
    public void getMemory(Context context) {
        loadStateObservableField.set(LoadState.LOADING);
        addDisposable(HttpManage.apiService.checkAccount(SPUtils.getInstance().getString(SpConst.WALLET_NAME))
                .compose(RxUtils.applySchedulers()).subscribe(jsonObject -> {
                    if (jsonObject.has("ram_usage") && jsonObject.get("ram_usage").getAsInt() > 0) {
                        if (jsonObject.get("ram_quota").getAsInt() == -1) {
                            remainingMemory =
                                    (context.getResources().getString(R.string.surplus) +
                                            df.format(jsonObject.get("ram_usage").getAsFloat() / 1000) + " KB" + "/8.00 KB");
                        } else {
                            remainingMemory =
                                    (context.getResources().getString(R.string.surplus) +
                                            df.format(jsonObject.get("ram_usage").getAsFloat() / 1000)
                                            + " KB" + "/" + df.format(jsonObject.get(
                                                    "ram_quota").getAsFloat() / 1000) + " KB");
                        }

                    }
                    notifyPropertyChanged(BR.remainingMemory);
                },e->{
                    LogUtils.d("获取内存信息失败");
                }));
    }

    //加载代币
    public void loadTokenList(Context context){

        loadStateObservableField.set(LoadState.LOADING);

        String appTokenJson = GsonUtils.getInstance().readJsonFile(context, "AppToken.json");

        List<AppTokenInfo> appTokenInfos = GsonUtils.getInstance().fromJson(appTokenJson, new TypeToken<List<AppTokenInfo>>() {
        }.getType());
        appTokenInfoObservableList.clear();
        appTokenInfoObservableList.addAll(appTokenInfos);
        loadStateObservableField.set(LoadState.SUCCESS);
        setRefreshState(RefreshState.REFRESH_END);
    }

    public void openAccountDetail(View view){
        ARouter.getInstance().build(RouteConst.AV_ACCOUNT_DETAIL).navigation(view.getContext());
    }
}
