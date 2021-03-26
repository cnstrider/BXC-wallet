package com.landis.eoswallet.ui.ringsign.viewmodel.item;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.landis.eoswallet.base.viewmodel.BaseItemViewModel;

public class ConfigAuthItemViewModel extends BaseItemViewModel<String> {

    public final ObservableField<String> actor = new ObservableField<>();

    @Override
    protected void setAllModel(@NonNull String s) {
        actor.set(s);
    }
}
