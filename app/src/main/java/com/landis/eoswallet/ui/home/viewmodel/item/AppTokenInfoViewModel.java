package com.landis.eoswallet.ui.home.viewmodel.item;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.base.viewmodel.BaseItemViewModel;
import com.landis.eoswallet.net.model.AppTokenInfo;


public class AppTokenInfoViewModel extends BaseItemViewModel<AppTokenInfo> {

    public final ObservableField<String> tokenName = new ObservableField<>();

    public final ObservableField<String> amount = new ObservableField<>();

    public final ObservableField<String> logo = new ObservableField<>();

    public final ObservableField<String> url = new ObservableField<>();
    public final ObservableField<Double> Rate = new ObservableField<>();

    @Override
    protected void setAllModel(@NonNull AppTokenInfo appTokenInfo) {
        tokenName.set(appTokenInfo.tokenname);
        amount.set(appTokenInfo.amount);
        logo.set(appTokenInfo.logo);
        url.set(appTokenInfo.url);
        Rate.set(appTokenInfo.Rate);
    }

    public void openWalletDetail(View view){
        AppTokenInfo appTokenInfo = getBaseItemModel();
        ARouter.getInstance().build(RouteConst.AV_WALLET_DETAIL)
                .withString(RouteConst.TOKEN_NAME,appTokenInfo.tokenname)
                .navigation(view.getContext());
    }
}
