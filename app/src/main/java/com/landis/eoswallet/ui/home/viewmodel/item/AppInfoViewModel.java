package com.landis.eoswallet.ui.home.viewmodel.item;

import android.content.Intent;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.base.viewmodel.BaseItemViewModel;
import com.landis.eoswallet.net.model.AppInfo;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

public class AppInfoViewModel extends BaseItemViewModel<AppInfo> {

    public final ObservableField<String> appName = new ObservableField<>();

    public final ObservableField<String> memo = new ObservableField<>();

    public final ObservableField<String> logo = new ObservableField<>();

    public final ObservableField<String> url = new ObservableField<>();

    public AppInfoViewModel() {
    }

    @Override
    protected void setAllModel(@NonNull AppInfo appInfo) {
        appName.set(appInfo.appname);
        memo.set(appInfo.memo);
        logo.set(appInfo.logo);
        url.set(appInfo.url);
    }

    public void onClickItem(){
        AppInfo appInfo = getBaseItemModel();
        if (appInfo.url.startsWith("http")) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(appInfo.url));
            startActivity(i);
        } else if (appInfo.url.startsWith("lospocket")) {
            if (appInfo.appname.equals("一键发币")) {
                ARouter.getInstance().build(RouteConst.AV_ISSUANCE).navigation();
            } else {
                ARouter.getInstance().build(Uri.parse(appInfo.url)).navigation();
            }
        }
    }
}
