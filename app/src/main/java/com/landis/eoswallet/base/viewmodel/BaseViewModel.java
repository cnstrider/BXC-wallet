package com.landis.eoswallet.base.viewmodel;


import android.arch.lifecycle.DefaultLifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.landis.eoswallet.BR;
import com.landis.eoswallet.enums.LoadState;
import com.landis.eoswallet.enums.RefreshState;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * viewModel的基类
 */
public class BaseViewModel extends BaseObservable implements DefaultLifecycleObserver {

    //加载状态
    public final ObservableField<LoadState> loadStateObservableField=new ObservableField<>();
    // 因为设置相同的值也要通知改变，所以采用@Bindable的方式
    private RefreshState refreshState;
    //是否还有更多数据
    private Boolean hasMore;

    private CompositeDisposable mCompositeDisposable;


    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        LogUtils.i(getClass().getSimpleName());
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        LogUtils.i(getClass().getSimpleName());
        // 取消所有的订阅，避免内存泄露
        if (null!=mCompositeDisposable){
            mCompositeDisposable.clear();
        }
    }

    /**
     * 获取加载状态
     * @return 加载状态
     */
    public LoadState getLoadState(){
        return loadStateObservableField.get();
    }

    /**
     * Bindable :此注解加在需要实现绑定观察的 get 或 is 开头的方法上，与 notifyPropertyChanged() 方法配合使用。
     *
     * 实体类需继承 BaseObseravle 或实现 Observable 接口的时候（必须），
     *
     * @Bindable 注解的方法必须是 public
     * @return
     */
    @Bindable
    public RefreshState getRefreshState(){
        return refreshState;
    }

    /**
     * 设置刷新状态
     * @param refreshState 刷新状态
     */
    protected void setRefreshState (RefreshState refreshState){
        this.refreshState=refreshState;
        notifyPropertyChanged(BR.refreshState);
    }

    @Bindable
    public Boolean getHasMore(){
        return hasMore;
    }

    /**
     * 设置加载更多
     * @param hasMore true 有更多数据   false 没有更多数据
     */
    protected void setHasMore(boolean hasMore){
        this.hasMore=hasMore;
        notifyPropertyChanged(BR.hasMore);
    }

    /**
     * 重新加载数据。没有网络，点击重试时回调
     */
    public void reloadData() {

    }

    /**
     * 添加订阅事件
     * @param disposable  订阅事件
     */
    protected void addDisposable(Disposable disposable){
        if (null==mCompositeDisposable){
            mCompositeDisposable=new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }
}
