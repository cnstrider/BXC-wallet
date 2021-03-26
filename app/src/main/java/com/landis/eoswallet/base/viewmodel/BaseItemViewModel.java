package com.landis.eoswallet.base.viewmodel;


import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

/**
 * item 的ViewModel的基类
 * @param <T>
 */
public abstract class BaseItemViewModel <T> extends BaseObservable {
    public final ObservableField<T> baseItemModel = new ObservableField<>();

    /**
     * 获取itemModel
     * @return
     */
    public T getBaseItemModel(){
        return baseItemModel.get();
    }

    /**
     * 设置item Model
     * @param t
     */
    public void setBaseItemModel(T t){
        baseItemModel.set(t);
        if (null!=t){
            setAllModel(t);
        }
    }

    /**
     * 设置所有的model。如果设置了基础model，那么会设置所有的model
     *
     * @param t 基础model
     */
    protected abstract void setAllModel(@NonNull T t);
}
