package com.landis.eoswallet.ui.home.viewmodel;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.net.Uri;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.reflect.TypeToken;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.enums.LoadState;
import com.landis.eoswallet.net.model.AppInfo;
import com.landis.eoswallet.util.GsonUtils;

import java.util.List;

public class AppViewModel extends BaseViewModel {

    public final ObservableList<Object> appInfoObservableList = new ObservableArrayList<>();

    public AppViewModel() {
    }

    public void loadAppData(Context context) {
        loadStateObservableField.set(LoadState.LOADING);

        String appInfoJson = GsonUtils.getInstance().readJsonFile(context, "AppInfo.json");

        List<AppInfo> appInfos = GsonUtils.getInstance().fromJson(appInfoJson,
                new TypeToken<List<AppInfo>>() {
        }.getType());
        AppInfo appInfo = new AppInfo();
        appInfo.appname = context.getResources().getString(R.string.node_vote);
        appInfo.memo = context.getResources().getString(R.string.node_vote);
        appInfo.url = "lospocket://route/vote/vote_home?";
        appInfos.add(0, appInfo);

        AppInfo appInfoRingSign = new AppInfo();
        appInfoRingSign.appname =
                context.getResources().getString(R.string.circular_signature_transfer);
        appInfoRingSign.memo =
                context.getResources().getString(R.string.circular_signature_transfer);
        appInfoRingSign.url = "lospocket://route/page/ring_sign?";
        appInfos.add(1, appInfoRingSign);

        if (null!=appInfoObservableList&&appInfoObservableList.size()>0){
            appInfoObservableList.clear();
        }

        appInfoObservableList.addAll(appInfos);
        loadStateObservableField.set(LoadState.SUCCESS);
    }

    /**
     * 搜索
     * @param searcheStr
     * @param searchView
     * @param textView
     */
    public void search(String searcheStr ,SearchView searchView, TextView textView){
        String url;
        if (searcheStr.startsWith("http://")) {
            url = "lospocket://route/page/web?url=" + searcheStr;
        } else {
            url = "lospocket://route/page/web?url=" + "http://" + searcheStr;
        }
        ARouter.getInstance().build(Uri.parse(url)).navigation();
        textView.setText("");
        searchView.clearFocus();
    }
}
