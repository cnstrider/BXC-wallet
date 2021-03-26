package com.landis.eoswallet;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.landis.eoswallet.net.manage.HttpManage;
import com.landis.eoswallet.util.LifecycleCallbacks;

import org.litepal.LitePal;

import io.reactivex.functions.BiConsumer;
import io.reactivex.plugins.RxJavaPlugins;

public class EosWalletApplication extends Application {

    private boolean isDebugARouter= true;

    @Override
    public void onCreate() {
        super.onCreate();
        if (isDebugARouter){
            ARouter.openLog();
            ARouter.openDebug();
        }
        HttpManage.init();
        ARouter.init((Application) getApplicationContext());
        registerActivityLifecycleCallbacks(LifecycleCallbacks.callback((new BiConsumer<Activity, Bundle>() {
            @Override
            public void accept(Activity activity, Bundle bundle) throws Exception {
            }
        })));
        RxJavaPlugins.setErrorHandler(throwable -> LogUtils.e(throwable.getMessage()));
        //数据库
        LitePal.initialize(getApplicationContext());
    }
}
