package com.landis.eoswallet.base.activity;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.databinding.ActivityBaseBinding;
import com.landis.eoswallet.databinding.ViewLoadErrorBinding;
import com.landis.eoswallet.databinding.ViewLoadingBinding;
import com.landis.eoswallet.databinding.ViewNoDataBinding;
import com.landis.eoswallet.databinding.ViewNoNetworkBinding;
import com.landis.eoswallet.enums.LoadState;
import com.landis.eoswallet.util.StatusBarUtil;
import com.landis.eoswallet.util.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;

public abstract class BaseActivity<DB extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity {

    protected DB mDataBinding;
    protected VM mViewModel;

    private ActivityBaseBinding mActivityBaseBinding;
    private ViewLoadingBinding mViewLoadingBinding;
    private ViewLoadErrorBinding mViewLoadErrorBinding;

    private ViewNoNetworkBinding mViewNoNetworkBinding;

    private ViewNoDataBinding mViewNoDataBinding;

    private Observable.OnPropertyChangedCallback mLoadStateCallback;

    private View appBarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarLightMode(this,true);
        mActivityBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base);
        mDataBinding = DataBindingUtil.inflate(getLayoutInflater(), getLayoutResId(),
                mActivityBaseBinding.flContentContainer, true);
        initViewModel();
        bindViewModel();

        initLoadState();
        init();

        // ViewModel订阅生命周期事件
        if (null != mViewModel) {
            getLifecycle().addObserver(mViewModel);
        }

        try{
            EventBus.getDefault().register(this);
        }catch (EventBusException eventBusException){
            LogUtils.e(eventBusException.getMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null==appBarView){
            return;
        }

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            int statusBarHeight = ViewUtils.getStatusBarHeight(this);
            appBarView.setPadding(0,statusBarHeight,0,0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mViewModel && isSupportLoad()) {
            mViewModel.loadStateObservableField.removeOnPropertyChangedCallback(mLoadStateCallback);
        }
        if (null!=mDataBinding){
            mDataBinding=null;
        }
        if (null!=appBarView){
            appBarView=null;
        }
        System.gc();
        EventBus.getDefault().unregister(this);
    }

    private void initLoadState() {

        if (null != mViewModel && isSupportLoad()) {
            mLoadStateCallback = new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    //切换加载view
                    switchLoadView(mViewModel.getLoadState());
                }
            };
            mViewModel.loadStateObservableField.addOnPropertyChangedCallback(mLoadStateCallback);
        }
    }

    /**
     * 设置appBarView
     * @param appBarView
     */
    protected void setAppBarView(View appBarView){
        this.appBarView=appBarView;
        StatusBarUtil.transparencyBar(this); //设置状态栏全透明
        StatusBarUtil.StatusBarLightMode(this, true); //设置白底黑字
    }

    /**
     * 移除加载view
     */
    private void removeLoadView() {
        int childCount = mActivityBaseBinding.flContentContainer.getChildCount();
        if (childCount > 1) {
            mActivityBaseBinding.flContentContainer.removeViews(1, childCount - 1);
        }
    }

    /**
     * 切换加载view
     *
     * @param loadState
     */
    private void switchLoadView(LoadState loadState) {
        removeLoadView();
        switch (loadState) {
            case LOADING:
                if (null == mViewLoadingBinding) {
                    mViewLoadingBinding = DataBindingUtil.inflate(getLayoutInflater(),
                            R.layout.view_loading, mActivityBaseBinding.flContentContainer, false);
                }
                mActivityBaseBinding.flContentContainer.addView(mViewLoadingBinding.getRoot());
                break;
            case NO_NETWORK:
                if (null == mViewNoNetworkBinding) {
                    mViewNoNetworkBinding = DataBindingUtil.inflate(getLayoutInflater(),
                            R.layout.view_no_network, mActivityBaseBinding.flContentContainer,
                            false);
                    mViewNoNetworkBinding.setViewModel(mViewModel);
                }
                mActivityBaseBinding.flContentContainer.addView(mViewNoNetworkBinding.getRoot());
                break;
            case NO_DATA:
                if (mViewNoDataBinding == null) {
                    mViewNoDataBinding = DataBindingUtil.inflate(getLayoutInflater(),
                            R.layout.view_no_data,
                            mActivityBaseBinding.flContentContainer, false);
                }
                mActivityBaseBinding.flContentContainer.addView(mViewNoDataBinding.getRoot());
                break;

            case ERROR:
                if (mViewLoadErrorBinding == null) {
                    mViewLoadErrorBinding = DataBindingUtil.inflate(getLayoutInflater(),
                            R.layout.view_load_error,
                            mActivityBaseBinding.flContentContainer, false);
                }
                mActivityBaseBinding.flContentContainer.addView(mViewLoadErrorBinding.getRoot());
                break;
        }
    }

    /**
     * 是否支持页面加载  默认不支持
     * @return true  支持  false 不支持
     */
    protected boolean isSupportLoad() {
        return true;
    }

    /**
     * 初始化
     */
    protected abstract void init();

    /**
     * 绑定viewModel
     */
    protected abstract void bindViewModel();

    /**
     * 初始化viewModel
     */
    protected abstract void initViewModel();

    /**
     * 获取当前布局资源ID
     * @return 布局资源Id
     */
    protected abstract int getLayoutResId();

}
