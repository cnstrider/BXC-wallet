package com.landis.eoswallet.ui.ringsign.viewmodel.item;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

import com.landis.eoswallet.base.viewmodel.BaseItemViewModel;
import com.landis.eoswallet.net.model.WeightInfo;

public class WeightItemViewModel extends BaseItemViewModel<WeightInfo> {

    public final ObservableField<String> actor = new ObservableField<>();
    public final ObservableInt weight = new ObservableInt();

    @Override
    protected void setAllModel(@NonNull WeightInfo weightInfo) {
        actor.set(weightInfo.actor);
        weight.set(weightInfo.weight);
    }
}
