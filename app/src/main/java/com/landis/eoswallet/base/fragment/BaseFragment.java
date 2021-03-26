package com.landis.eoswallet.base.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.blankj.utilcode.util.BarUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.databinding.FragmentBaseBinding;
import com.landis.eoswallet.databinding.ViewLoadErrorBinding;
import com.landis.eoswallet.databinding.ViewLoadingBinding;
import com.landis.eoswallet.databinding.ViewNoNetworkBinding;
import com.landis.eoswallet.databinding.ViewNoDataBinding;
import com.landis.eoswallet.enums.LoadState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;

/**
 * fragment 基类
 * @param <DB> data binding
 * @param <VM> viewModel
 */
public abstract class BaseFragment<DB extends ViewDataBinding, VM extends BaseViewModel> extends Fragment {

    protected DB mDataBinding;

    protected VM mViewModel;

    private FragmentBaseBinding mFragmentBaseBinding;
    private ViewLoadingBinding mViewLoadingBinding;

    private ViewLoadErrorBinding mViewLoadErrorBinding;

    private ViewNoNetworkBinding mViewNoNetworkBinding;

    private ViewNoDataBinding mViewNoDataBinding;

    private Observable.OnPropertyChangedCallback mLoadStateCallback;

    public ViewGroup appBarView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViewModel();

        //viewModel订阅生命周期事件
        if (null != mViewModel) {
            getLifecycle().addObserver(mViewModel);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mFragmentBaseBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_base,container, false);
        mDataBinding = DataBindingUtil.inflate(inflater, getLayoutResId(),
                mFragmentBaseBinding.flContentContainer, true);
        bindViewModel();
        initLoadState();
        init();
        return mFragmentBaseBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            EventBus.getDefault().register(this);
        } catch (EventBusException ex) {
//                if (!ex.getStackTrace()[0].getMethodName().equals("findSubscriberMethods")) {
//                    throw ex;
//                }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null!=mViewModel&&isSupportLoad()){
            //移除回调
            mViewModel.loadStateObservableField.removeOnPropertyChangedCallback(mLoadStateCallback);
        }
        EventBus.getDefault().unregister(this);
    }

    private void initLoadState() {
        if (null!=mViewModel&&isSupportLoad()){
            mLoadStateCallback=new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    switchLoadView(mViewModel.getLoadState());
                }
            };
            //添加回调
            mViewModel.addOnPropertyChangedCallback(mLoadStateCallback);
        }

    }

    private void switchLoadView(LoadState loadState) {
        removeLoadView();

        switch (loadState) {
            case LOADING:
                if (mViewLoadingBinding == null) {
                    mViewLoadingBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_loading,
                            mFragmentBaseBinding.flContentContainer, false);
                }
                mFragmentBaseBinding.flContentContainer.addView(mViewLoadingBinding.getRoot());
                break;

            case NO_NETWORK:
                if (mViewNoNetworkBinding == null) {
                    mViewNoNetworkBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_no_network,
                            mFragmentBaseBinding.flContentContainer, false);
                    mViewNoNetworkBinding.setViewModel(mViewModel);
                }
                mFragmentBaseBinding.flContentContainer.addView(mViewNoNetworkBinding.getRoot());
                break;

            case NO_DATA:
                if (mViewNoDataBinding == null) {
                    mViewNoDataBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_no_data,
                            mFragmentBaseBinding.flContentContainer, false);
                }
                mFragmentBaseBinding.flContentContainer.addView(mViewNoDataBinding.getRoot());
                break;

            case ERROR:
                if (mViewLoadErrorBinding == null) {
                    mViewLoadErrorBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_load_error,
                            mFragmentBaseBinding.flContentContainer, false);
                }
                mFragmentBaseBinding.flContentContainer.addView(mViewLoadErrorBinding.getRoot());
                break;

            default:
                break;
        }
    }

    private void removeLoadView() {

        int childCount = mFragmentBaseBinding.flContentContainer.getChildCount();
        if (childCount > 1) {
            mFragmentBaseBinding.flContentContainer.removeViews(1, childCount - 1);
        }
    }

    /**
     * 是否支持页面加载。默认不支持
     * @return true表示支持，false表示不支持
     */
    protected boolean isSupportLoad() {

        return false;
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
     * 获取布局文件ID
     * @return 布局文件ID
     */
    protected abstract int getLayoutResId();

    /**
     * 初始化viewModel
     */
    protected abstract void initViewModel();

    public void setAppBarView(ViewGroup appBarView) {
        this.appBarView = appBarView;
    }

    public void addPaddingTopEqualStatusBarHeight(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int height = BarUtils.getStatusBarHeight();
            appBarView.setPadding(0, height, 0, 0);
        }
    }
}
