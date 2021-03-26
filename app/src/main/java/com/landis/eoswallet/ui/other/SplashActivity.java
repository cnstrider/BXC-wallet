package com.landis.eoswallet.ui.other;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SPUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.util.RxUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SplashActivity extends Activity {

    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_EosWallet);
        BarUtils.setNavBarVisibility(this,false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            BarUtils.setNavBarImmersive(this);
        }
        BarUtils.setNotificationBarVisibility(false);
        setContentView(R.layout.activity_splash);

        if (null!=disposable&&!disposable.isDisposed()){
            return;
        }

        disposable= Observable.interval(1,500, TimeUnit.MILLISECONDS)
                .compose(RxUtils.applySchedulers()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                if (aLong>=4){
                    SPUtils.getInstance().put("IS_FIRST", false);
                    disposable.dispose();

                    ARouter.getInstance().build(RouteConst.AV_ACCOUNT).navigation(SplashActivity.this);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null!=disposable||!disposable.isDisposed()){
            disposable.dispose();
            disposable=null;
        }
    }
}
