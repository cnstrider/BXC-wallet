package com.landis.eoswallet.base.fragment;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;


import com.landis.eoswallet.base.viewmodel.BaseViewModel;

/**
 * 懒加载fragment
 * @param <DB> data binding
 * @param <VM> view model
 */
public abstract class BaseLazyFragment <DB extends ViewDataBinding,VM extends BaseViewModel> extends BaseFragment{
    //用户是否可见
    protected  boolean mVisibleToUser;
    //view是否创建
    protected boolean mViewCreated;
    //是否加载数据
    protected boolean mLazyLoaded;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mVisibleToUser=isVisibleToUser;
        lazyLoad();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewCreated=true;
        lazyLoad();
    }

    /**
     * 判断是否加懒载数据
     */
    private void lazyLoad() {
        if (mVisibleToUser&&mViewCreated&&!mLazyLoaded){
            mLazyLoaded=true;
            onLazyLoad();
        }
    }

    /**
     * 加载数据
     */
    protected abstract void onLazyLoad();
}
